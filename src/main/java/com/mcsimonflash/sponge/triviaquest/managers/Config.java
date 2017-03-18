package com.mcsimonflash.sponge.triviaquest.managers;

import com.google.common.collect.Lists;

import com.mcsimonflash.sponge.triviaquest.TriviaQuest;
import com.mcsimonflash.sponge.triviaquest.objects.TriviaQuestion;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import org.spongepowered.api.Sponge;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

@ConfigSerializable
public class Config {
    private static ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder()
            .setPath(TriviaQuest.getPlugin().getDefaultConfig())
            .build();
    private static CommentedConfigurationNode rootNode;

    private static int triviaLength = 30;
    private static int triviaInterval = 300;
    private static List<String> triviaReward = Lists.newArrayList();
    private static List<String> enabledPacksList = Lists.newArrayList();

    public static int getTriviaLength() {
        return triviaLength;
    }

    public static int getTriviaInterval() {
        return triviaInterval;
    }

    public static String getTriviaReward() {
        return triviaReward.get(new Random().nextInt(triviaReward.size()));
    }

    public static void readConfig() {
        if (Files.notExists(TriviaQuest.getPlugin().getDefaultConfig())) {
            try {
                Sponge.getAssetManager().getAsset(TriviaQuest.getPlugin(), "defaultConfig.conf").get().copyToFile(TriviaQuest.getPlugin().getDefaultConfig());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadConfig();

        triviaLength = rootNode.getNode("Settings", "triviaLength").getInt(30);
        triviaInterval = rootNode.getNode("Settings", "triviaInterval").getInt(300);
        Map<Object, ? extends ConfigurationNode> rewardsMap = rootNode.getNode("Rewards").getChildrenMap();
        for (Map.Entry<Object, ? extends ConfigurationNode> reward : rewardsMap.entrySet()) {
            String rewardCommand = rootNode.getNode("Rewards", (String) reward.getKey()).getString("");
            if (rewardCommand.substring(0, 1).equals("/")) {
                rewardCommand = rewardCommand.substring(1);
            }
            triviaReward.add(rewardCommand);
        }

        if (triviaInterval < triviaLength) {
            TriviaQuest.getPlugin().getLogger().error("[ERROR]: triviaInterval is less than triviaLength! Now running on defaults.");
        }

        String enabledPacks = rootNode.getNode("Settings", "enabledPacks").getString("Default");
        Collections.addAll(enabledPacksList, enabledPacks.split(", "));

        Map<Object, ? extends ConfigurationNode> triviaPackMap = rootNode.getNode("TriviaQuestions").getChildrenMap();
        for (Map.Entry<Object, ? extends ConfigurationNode> triviaPack : triviaPackMap.entrySet()) {
            if (isPackEnabled((String) triviaPack.getKey())) {
                Map<Object, ? extends ConfigurationNode> packQuestionMap = rootNode.getNode("TriviaQuestions", (String) triviaPack.getKey()).getChildrenMap();
                for (Map.Entry<Object, ? extends ConfigurationNode> packQuestion : packQuestionMap.entrySet()) {
                    String quesAnsPair = rootNode.getNode("TriviaQuestions", (String) triviaPack.getKey(), (String) packQuestion.getKey()).getString();
                    String ques = quesAnsPair.substring(quesAnsPair.indexOf("{") + 1, quesAnsPair.indexOf("},"));
                    String ans = quesAnsPair.substring(quesAnsPair.indexOf(",{") + 2, quesAnsPair.indexOf("}", quesAnsPair.indexOf(",{")));
                    if (ques.isEmpty() || ans.isEmpty())
                        TriviaQuest.getPlugin().getLogger().error("TriviaQuestions " + triviaPack + " " + packQuestion + " configured improperly!");
                    else
                        RunTrivia.getTriviaList().add(new TriviaQuestion(ques, ans));
                }
            }
        }
        RunTrivia.shuffle();

        if (saveConfig()) {
            TriviaQuest.getPlugin().getLogger().info("Config successfully saved");
        } else {
            TriviaQuest.getPlugin().getLogger().error("[ERROR]: Config could not save!");
        }
    }

    private static void loadConfig() {
        try {
            rootNode = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean saveConfig() {
        try {
            loader.save(rootNode);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isPackEnabled(String packName) {
        for (String pack : enabledPacksList) {
            if (pack.equalsIgnoreCase(packName)) {
                return true;
            }
        }
        return false;
    }
}