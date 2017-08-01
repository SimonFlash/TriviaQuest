package com.mcsimonflash.sponge.triviaquest.managers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.mcsimonflash.sponge.triviaquest.TriviaQuest;
import com.mcsimonflash.sponge.triviaquest.objects.Completion;
import com.mcsimonflash.sponge.triviaquest.objects.Question;
import com.mcsimonflash.sponge.triviaquest.objects.Scramble;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Config {

    private static Path coreDir = TriviaQuest.getPlugin().getDirectory().resolve("triviaquest");
    private static Path packDir = coreDir.resolve("packs");
    private static CommentedConfigurationNode coreNode;

    public static int chanceSum;
    public static int enableRewardsCount;
    public static int enableTriviaCount;
    public static int triviaInterval;
    public static int triviaLength;
    public static List<String> enabledPacks = Lists.newArrayList();
    public static Map<String, Integer> rewardCommands = Maps.newHashMap();

    private static boolean initializeConfig() {
        try {
            Files.createDirectories(coreDir);
            Path core = coreDir.resolve("triviaquest.core");
            if (Files.notExists(core)) {
                Sponge.getAssetManager().getAsset(TriviaQuest.getPlugin(),"triviaquest.core").get().copyToFile(core);
                TriviaQuest.getPlugin().getLogger().warn("Created main directory and initiated triviaquest.core.");
            }
            if (Files.notExists(packDir)) {
                Files.createDirectories(packDir);
                Sponge.getAssetManager().getAsset(TriviaQuest.getPlugin(), "trivia.pack").get().copyToFile(packDir.resolve("trivia.pack"));
                TriviaQuest.getPlugin().getLogger().warn("Created pack directory and initiated trivia.pack.");
            }
            coreNode = HoconConfigurationLoader.builder().setPath(core).build().load();
        } catch (IOException e) {
            TriviaQuest.getPlugin().getLogger().error("Config could not be loaded!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean readConfig() {
        if (initializeConfig()) {
            testLegacy();
            try {
                enabledPacks = coreNode.getNode("enabled-packs").getList(TypeToken.of(String.class));
            } catch (ObjectMappingException ignored) {
                TriviaQuest.getPlugin().getLogger().error("Error loading enabled packs list!");
                return false;
            }
            System.out.println(String.join(", ", enabledPacks));
            enabledPacks.forEach(p -> loadPack(packDir.resolve(p + ".pack")));
            coreNode.getNode("rewards").getChildrenMap().values().forEach(r -> rewardCommands.put(r.getNode("command").getString(""), r.getNode("chance").getInt(0)));
            chanceSum = rewardCommands.values().stream().mapToInt(Integer::intValue).sum();
            enableRewardsCount = coreNode.getNode("config", "enable-rewards-count").getInt(0);
            enableTriviaCount = coreNode.getNode("config", "enable-trivia-count").getInt(0);
            triviaInterval = coreNode.getNode("config", "trivia-interval").getInt(300);
            triviaLength = coreNode.getNode("config", "trivia-length").getInt(30);
            if (triviaInterval < 1 || triviaLength < 1) {
                TriviaQuest.getPlugin().getLogger().error("Interval and length must be at least 1! | Interval:[" + triviaInterval + "] Length:[" + triviaLength + "]");
                return false;
            }
            Trivia.prefix = Util.toText(coreNode.getNode("config", "trivia-prefix").getString("&8&l[&5TriviaQuest&8&l]&f "));
            if (coreNode.getNode("config", "enable-on-startup").getBoolean(false)) {
                Trivia.startRunner();
            }
        }
        return false;
    }

    public static void loadPack(Path path) {
        if (Files.exists(path)) {
            try {
                CommentedConfigurationNode packNode = HoconConfigurationLoader.builder().setPath(path).build().load();
                packNode.getNode("trivia", "completions").getChildrenMap().values().forEach(q -> {
                    try {
                        Trivia.triviaList.add(new Completion(q.getNode("word").getString(""), q.getNode("choices").getList(TypeToken.of(String.class))));
                    } catch (ObjectMappingException ignored) {
                        TriviaQuest.getPlugin().getLogger().error("Error loading choices list! | Question:[" + q.getKey() + "] Pack:[" + packNode.getKey());
                    }
                });
                packNode.getNode("trivia", "questions").getChildrenMap().values().forEach(q -> {
                    try {
                        Trivia.triviaList.add(new Question(q.getNode("question").getString(""), q.getNode("answers").getList(TypeToken.of(String.class))));
                    } catch (ObjectMappingException ignored) {
                        TriviaQuest.getPlugin().getLogger().error("Error loading answers list! | Question:[" + q.getKey() + "] Pack:[" + packNode.getKey());
                    }
                });
                packNode.getNode("trivia", "scrambles").getChildrenMap().values().forEach(q -> {
                    try {
                        Trivia.triviaList.add(new Scramble(q.getNode("word").getString(""), q.getNode("choices").getList(TypeToken.of(String.class))));
                    } catch (ObjectMappingException ignored) {
                        TriviaQuest.getPlugin().getLogger().error("Error loading choices list! | Question:[" + q.getKey() + "] Pack:[" + packNode.getKey());
                    }
                });
            } catch (IOException e) {
                TriviaQuest.getPlugin().getLogger().error("Pack could not be loaded! | Pack:[" + path.getFileName() + "]");
                e.printStackTrace();
            }
        } else {
            TriviaQuest.getPlugin().getLogger().error("Attempted to load non-existent pack " + path.getFileName() + "!");
        }
    }

    public static void testLegacy() {
        if (coreNode.getNode("-legacy").getBoolean(false)) {
            TriviaQuest.getPlugin().getLogger().warn("Attempting to update Legacy config");
            try {
                CommentedConfigurationNode oldNode = HoconConfigurationLoader.builder().setPath(TriviaQuest.getPlugin().getDirectory().resolve("triviaquest.conf")).build().load();
                for (CommentedConfigurationNode pack : oldNode.getNode("trivia").getChildrenMap().values()) {
                    TriviaQuest.getPlugin().getLogger().warn("Converting pack " + pack.getKey());
                    Path newPack = packDir.resolve(pack.getKey() + ".pack");
                    if (Files.notExists(newPack)) {
                        Map<String, Question> questions = Maps.newHashMap();
                        for (CommentedConfigurationNode question : pack.getChildrenMap().values()) {
                            String quesAnsStr = question.getString("");
                            quesAnsStr = quesAnsStr.startsWith("(") ? quesAnsStr.substring(1) : quesAnsStr;
                            quesAnsStr = quesAnsStr.endsWith(")") ? quesAnsStr.substring(0, quesAnsStr.length() - 1) : quesAnsStr;
                            String[] split = quesAnsStr.split("\\),\\(");
                            questions.put((String) question.getKey(), new Question(split[0], Arrays.asList(Arrays.copyOfRange(split, 1, split.length))));
                        }
                        HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setPath(packDir.resolve(pack.getKey() + ".pack")).build();
                        CommentedConfigurationNode packNode = loader.load();
                        questions.forEach((k, q) -> {
                            packNode.getNode("trivia", "questions", k, "question").setValue(q.Question);
                            packNode.getNode("trivia", "questions", k, "answers").setValue(q.Answers);
                        });
                        loader.save(packNode);
                    } else {
                        TriviaQuest.getPlugin().getLogger().error("A pack file exists for " + pack.getKey() + "! Conversion of this pack has been stopped.");
                    }
                }
                try {
                    coreNode.getNode("enabled-packs").setValue(oldNode.getNode("config", "enabled_packs").getList(TypeToken.of(String.class)));
                } catch (ObjectMappingException ex) {
                    TriviaQuest.getPlugin().getLogger().error("Could not retrieve enabled-packs list.");
                }
                coreNode.getNode("-legacy").setValue(false);
                HoconConfigurationLoader.builder().setPath(coreDir.resolve("triviaquest.core")).build().save(coreNode);
            } catch (IOException e) {
                TriviaQuest.getPlugin().getLogger().error("Unable to load/save new pack file!");
                e.printStackTrace();
            }
            TriviaQuest.getPlugin().getLogger().warn("Config conversion has concluded.");
        }
    }
}