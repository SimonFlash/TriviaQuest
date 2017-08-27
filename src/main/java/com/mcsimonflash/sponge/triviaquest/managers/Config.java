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

    public static boolean showAnswers;
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
            showAnswers = coreNode.getNode("config", "show-answers").getBoolean(false);
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
}