package com.mcsimonflash.sponge.triviaquest.managers;

import com.google.common.collect.Lists;

import com.mcsimonflash.sponge.triviaquest.TriviaQuest;
import com.mcsimonflash.sponge.triviaquest.objects.TriviaQuestion;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Config {
    private static ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder()
            .setPath(TriviaQuest.getPlugin().getDefaultConfig())
            .build();
    private static CommentedConfigurationNode rootNode;

    private static int triviaInterval = 300;
    private static int triviaLength = 30;
    private static Text triviaPrefix = Text.of("");
    private static List<String> triviaRewardList = Lists.newArrayList();
    private static List<String> enabledPacksList = Lists.newArrayList();

    public static void readConfig() {
        if (Files.notExists(TriviaQuest.getPlugin().getDefaultConfig())) {
            try {
                Sponge.getAssetManager().getAsset(TriviaQuest.getPlugin(), "defaultConfig.conf").get().copyToFile(TriviaQuest.getPlugin().getDefaultConfig());
                TriviaQuest.getPlugin().getLogger().warn("Default Config loaded! Edit triviaquest.conf to create questions, add rewards, and change config settings!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadConfig();
        
        RunTrivia.getTriviaList().clear();
        triviaRewardList.clear();

        String enabledPacks = rootNode.getNode("config", "enabled_packs").getString("Default");
        Collections.addAll(enabledPacksList, enabledPacks.split(", "));

        triviaInterval = rootNode.getNode("config", "trivia_interval").getInt(300);
        triviaLength = rootNode.getNode("config", "trivia_length").getInt(30);
        triviaPrefix = TextSerializers.FORMATTING_CODE.deserialize(rootNode.getNode("config", "trivia_prefix").getString("&8[&5TriviaQuest&8] "));

        if (triviaInterval < triviaLength) {
            TriviaQuest.getPlugin().getLogger().error("[ERROR]: interval is less than length! Running on defaults.");
            triviaInterval = 300;
            triviaLength = 30;
        }

        Map<Object, ? extends ConfigurationNode> rewardsMap = rootNode.getNode("rewards").getChildrenMap();
        for (Map.Entry<Object, ? extends ConfigurationNode> reward : rewardsMap.entrySet()) {
            String rewardCommand = rootNode.getNode("rewards", (String) reward.getKey()).getString("");
            if (rewardCommand.substring(0, 1).equals("/")) {
                rewardCommand = rewardCommand.substring(1);
            }
            triviaRewardList.add(rewardCommand);
        }

        Map<Object, ? extends ConfigurationNode> triviaPackMap = rootNode.getNode("trivia").getChildrenMap();
        for (Map.Entry<Object, ? extends ConfigurationNode> triviaPack : triviaPackMap.entrySet()) {
            if (isPackEnabled((String) triviaPack.getKey())) {
                Map<Object, ? extends ConfigurationNode> packQuestionMap = rootNode.getNode("trivia", (String) triviaPack.getKey()).getChildrenMap();
                for (Map.Entry<Object, ? extends ConfigurationNode> packQuestion : packQuestionMap.entrySet()) {
                    String quesAnsStr = rootNode.getNode("trivia", (String) triviaPack.getKey(), (String) packQuestion.getKey()).getString();
                    List<String> quesAnsList = Lists.newArrayList();
                    Collections.addAll(quesAnsList, quesAnsStr.substring(1, quesAnsStr.length()-1).split( "\\),\\("));
                    if (quesAnsList.size() < 2) {
                        TriviaQuest.getPlugin().getLogger().error("trivia " + triviaPack + " " + packQuestion + " configured improperly!");
                    } else {
                        TriviaQuest.getPlugin().getLogger().info("Adding " + String.join(", ", quesAnsList));
                        RunTrivia.getTriviaList().add(new TriviaQuestion(quesAnsList));
                    }
                }
            }
        }
        if (RunTrivia.getTriviaList().isEmpty()) {
            TriviaQuest.getPlugin().getLogger().warn("No trivia questions have been loaded!");
            RunTrivia.getTriviaList().add(new TriviaQuestion(Lists.newArrayList("Who is the greatest person who ever lived?", "Simon_Flash")));
        } else {
            RunTrivia.shuffle();
            TriviaQuest.getPlugin().getLogger().info("Questions successfully loaded!");
        }

        if (!saveConfig()) {
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

    public static int getTriviaInterval() {
        return triviaInterval;
    }
    public static int getTriviaLength() {
        return triviaLength;
    }
    public static Text getTriviaPrefix() {
        return triviaPrefix;
    }
    public static String getTriviaReward() {
        return triviaRewardList.get(new Random().nextInt(triviaRewardList.size()));
    }
}