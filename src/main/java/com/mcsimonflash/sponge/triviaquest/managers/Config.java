package com.mcsimonflash.sponge.triviaquest.managers;

import com.google.common.collect.Lists;

import com.google.common.reflect.TypeToken;
import com.mcsimonflash.sponge.triviaquest.TriviaQuest;
import com.mcsimonflash.sponge.triviaquest.objects.TriviaQuestion;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Config {
    private static ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder()
            .setPath(TriviaQuest.getPlugin().getDefaultConfig())
            .build();
    private static CommentedConfigurationNode rootNode;

    private static void loadConfig() {
        try {
            rootNode = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            TriviaQuest.getPlugin().getLogger().error("Config could not load!");
        }
    }

    private static void saveConfig() {
        try {
            loader.save(rootNode);
        } catch (IOException e) {
            e.printStackTrace();
            TriviaQuest.getPlugin().getLogger().error("Config could not save!");
        }
    }

    private static int triviaInterval = 300;
    private static int triviaLength = 30;

    public static void readConfig() {
        if (Files.notExists(TriviaQuest.getPlugin().getDefaultConfig())) {
            try {
                Sponge.getAssetManager().getAsset(TriviaQuest.getPlugin(), "defaultConfig.conf").get().copyToFile(TriviaQuest.getPlugin().getDefaultConfig());
                TriviaQuest.getPlugin().getLogger().warn("Default Config loaded! Edit triviaquest.conf to change settings and create tasks!");
            } catch (IOException e) {
                e.printStackTrace();
                TriviaQuest.getPlugin().getLogger().error("Unable to clone asset! Default config must be copied in manually");
                return;
            }
        }
        loadConfig();
        triviaInterval = rootNode.getNode("config", "trivia_interval").getInt(300);
        triviaLength = rootNode.getNode("config", "trivia_length").getInt(30);
        if (triviaInterval < triviaLength) {
            TriviaQuest.getPlugin().getLogger().error("Interval is less than length! Running on defaults.");
            triviaInterval = 300;
            triviaLength = 30;
        }
        loadQuestions();
    }

    public static void loadQuestions() {
        loadConfig();
        Trivia.getTriviaList().clear();
        Map<Object, ? extends ConfigurationNode> triviaPackMap = rootNode.getNode("trivia").getChildrenMap();
        for (Map.Entry<Object, ? extends ConfigurationNode> triviaPack : triviaPackMap.entrySet()) {
            if (isPackEnabled((String) triviaPack.getKey())) {
                Map<Object, ? extends ConfigurationNode> packQuestionMap = rootNode.getNode("trivia", triviaPack.getKey()).getChildrenMap();
                for (Map.Entry<Object, ? extends ConfigurationNode> packQuestion : packQuestionMap.entrySet()) {
                    String quesAnsStr = rootNode.getNode("trivia", triviaPack.getKey(), packQuestion.getKey()).getString();
                    if (quesAnsStr.length() >= 7 && quesAnsStr.contains("),(")) {
                        List<String> quesAnsList = Lists.newArrayList();
                        Collections.addAll(quesAnsList, quesAnsStr.substring(1, quesAnsStr.length() - 1).split("\\),\\("));
                        if (quesAnsList.size() > 1) {
                            Trivia.getTriviaList().add(new TriviaQuestion(quesAnsList));
                            continue;
                        }
                    }
                    TriviaQuest.getPlugin().getLogger().error("Trivia " + triviaPack + " " + packQuestion + " configured improperly!");
                }
            }
        }
        if (Trivia.getTriviaList().isEmpty()) {
            TriviaQuest.getPlugin().getLogger().warn("No trivia questions have been loaded!");
            Trivia.getTriviaList().add(new TriviaQuestion(Lists.newArrayList("Who is the greatest person who ever lived?", "Simon_Flash")));
        }
        Trivia.shuffle();
    }

    public static int getTriviaInterval() {
        return triviaInterval;
    }

    public static int getTriviaLength() {
        return triviaLength;
    }

    public static Text getTriviaPrefix() {
        return TextSerializers.FORMATTING_CODE.deserialize(rootNode.getNode("config", "trivia_prefix").getString("&8[&5TriviaQuest&8] "));
    }

    public static String getTriviaReward() {
        loadConfig();
        List<String> triviaRewardList = Lists.newArrayList();
        try {
            triviaRewardList = rootNode.getNode("config", "rewards").getList(TypeToken.of(String.class));
        } catch (ObjectMappingException ignored) {
            TriviaQuest.getPlugin().getLogger().error("Unable to read rewards list!");
        }
        if (triviaRewardList.isEmpty()) {
            return null;
        }
        return triviaRewardList.get(new Random().nextInt(triviaRewardList.size()));
    }

    public static boolean packExists(String packName) {
        Map<Object, ? extends ConfigurationNode> triviaPackMap = rootNode.getNode("trivia").getChildrenMap();
        for (Map.Entry<Object, ? extends ConfigurationNode> triviaPack : triviaPackMap.entrySet()) {
            if (((String) triviaPack.getKey()).equalsIgnoreCase(packName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPackEnabled(String packName) {
        loadConfig();
        List<String> enabledPacksList = Lists.newArrayList();
        try {
            enabledPacksList = rootNode.getNode("config", "enabled_packs").getList(TypeToken.of(String.class));
        } catch (ObjectMappingException e) {
            TriviaQuest.getPlugin().getLogger().error("Unable to parse enabled_packs list!");
        }
        return enabledPacksList.contains(packName);
    }

    public static void enablePack(String packName) {
        loadConfig();
        try {
            List<String> enabledPacksList = rootNode.getNode("config", "enabled_packs").getList(TypeToken.of(String.class));
            enabledPacksList.add(packName);
            rootNode.getNode("config", "enabled_packs").setValue(enabledPacksList);
        } catch (ObjectMappingException ignored) {
            TriviaQuest.getPlugin().getLogger().error("Unable to parse enabled_packs list!");
        }
        saveConfig();
    }

    public static void disablePack(String packName) {
        loadConfig();
        try {
            List<String> enabledPacksList = rootNode.getNode("config", "enabled_packs").getList(TypeToken.of(String.class));
            enabledPacksList.remove(packName);
            rootNode.getNode("config", "enabled_packs").setValue(enabledPacksList);
        } catch (ObjectMappingException ignored) {
            TriviaQuest.getPlugin().getLogger().error("Unable to parse enabled_packs list!");
        }
        saveConfig();
    }

    public static boolean isCheckMessages() {
        loadConfig();
        return rootNode.getNode("config", "check_messages").getBoolean(true);
    }

    public static boolean isShowAnswer() {
        loadConfig();
        return rootNode.getNode("config", "show_answer").getBoolean(false);
    }
}