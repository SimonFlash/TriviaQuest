package com.mcsimonflash.sponge.triviaquest.managers;

import com.google.common.collect.Lists;

import com.google.common.collect.Maps;
import com.mcsimonflash.sponge.triviaquest.TriviaQuest;
import com.mcsimonflash.sponge.triviaquest.objects.TriviaQuestion;
import com.mcsimonflash.sponge.triviaquest.objects.TriviaTask;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Trivia {

    private static Task runnerTask;
    private static Task triviaTask;
    private static int triviaIndex = 0;
    private static boolean triviaRunning = false;
    private static List<TriviaQuestion> triviaList = Lists.newArrayList();
    private static Map<String, TriviaQuestion> playerTriviaMap = Maps.newLinkedHashMap();
    private static TriviaQuestion triviaQuestion;

    public static void startRunner(int delay) {
        triviaRunning = true;
        runnerTask = Task.builder().execute(
                task -> {
                    if (!isTriviaActive()) {
                        if (playerTriviaMap.isEmpty()) {
                            askQuestion();
                        } else {
                            askPlayerQuestion(0);
                        }
                    }
                })
                .name("triviaTask")
                .delay(delay, TimeUnit.SECONDS)
                .interval(Config.getTriviaInterval(), TimeUnit.SECONDS)
                .submit(TriviaQuest.getPlugin());
    }

    public static void stopRunner(boolean suspend) {
        runnerTask.cancel();
        triviaRunning = suspend;
    }

    public static void shuffle() {
        Collections.shuffle(triviaList);
    }

    public static void nextQuestion() {
        triviaQuestion = triviaList.get(triviaIndex++);
        if (triviaIndex == triviaList.size()) {
            triviaIndex = 0;
            shuffle();
        }
    }

    public static void askQuestion() {
        nextQuestion();
        triviaTask = Task.builder().execute(new TriviaTask(null))
                .name("triviaTask")
                .delay(0, TimeUnit.SECONDS)
                .interval(1, TimeUnit.SECONDS)
                .submit(TriviaQuest.getPlugin());
    }

    public static void askPlayerQuestion(int delay) {
        String name = playerTriviaMap.keySet().iterator().next();
        triviaQuestion = playerTriviaMap.remove(name);
        triviaTask = Task.builder().execute(new TriviaTask(name))
                .name("triviaTask")
                .delay(delay, TimeUnit.SECONDS)
                .interval(1, TimeUnit.SECONDS)
                .submit(TriviaQuest.getPlugin());
    }

    public static void registerQuestion(String name, TriviaQuestion trivia) {
        playerTriviaMap.put(name, trivia);
        if (triviaRunning) {
            stopRunner(true);
        }
        if (!isTriviaActive()) {
            askPlayerQuestion(1);
        }
    }

    public static void unregisterQuestion(String name) {
        playerTriviaMap.remove(name);
    }

    public static boolean checkAnswer(CommandSource src, String playerAnswer) {
        boolean correctAnswer = false;
        for (String ans : triviaQuestion.getAnswer()) {
            if (playerAnswer.equalsIgnoreCase(ans)) {
                correctAnswer = true;
                break;
            }
        }
        if (correctAnswer) {
            String rewardCmd = null;
            String sourceName;
            Text rewardMsg;
            if (src instanceof Player) {
                sourceName = src.getName();
                rewardMsg = Text.of(Config.getTriviaPrefix(),
                        TextColors.WHITE, "Nice job! Here's your reward!");
                rewardCmd = Config.getTriviaReward();
            } else if (src instanceof ConsoleSource) {
                sourceName = ("GLaDOS");
                rewardMsg = Text.of(Config.getTriviaPrefix(),
                        TextColors.WHITE, "Aww, I can't give you a reward :(");
            } else {
                sourceName = ("The Companion Cube");
                rewardMsg = Text.of(TextColors.DARK_RED, "I will find you, and I will kill you.");
            }
            Text broadcast = Text.of(Config.getTriviaPrefix(), TextColors.LIGHT_PURPLE, sourceName, TextColors.WHITE, " got it! ");
            if (Config.isShowAnswer()) {
                if (triviaQuestion.getAnswer().size() == 1) {
                    broadcast = broadcast.concat(Text.of("The answer was ", TextColors.LIGHT_PURPLE, String.join("",triviaQuestion.getAnswer()), "!"));
                } else {
                    broadcast = broadcast.concat(Text.of("The answers were ", TextColors.LIGHT_PURPLE, String.join(", ", triviaQuestion.getAnswer()), "!"));
                }
            } else {
                broadcast = broadcast.concat(Text.of("Better luck next time!"));
            }
            Sponge.getServer().getBroadcastChannel().send(broadcast);
            if (!triviaQuestion.isGiveReward() && rewardCmd != null) {
                src.sendMessage(rewardMsg);
                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), rewardCmd.replaceAll("\\{player}", src.getName()));
            }
            endQuestion();
            if (playerTriviaMap.isEmpty()) {
                if (triviaRunning) {
                    startRunner(Config.getTriviaInterval());
                }
            } else {
                askPlayerQuestion(Config.getPlayerDelay());
            }
            return true;
        } else {
            return false;
        }
    }

    public static void endQuestion() {
        triviaTask.cancel();
        removeTriviaQuestion();
    }

    public static boolean isRunnerActive() {
        return triviaRunning;
    }

    public static boolean isTriviaActive() {
        return triviaQuestion != null;
    }

    public static boolean askedQuestion(String name) {
        return playerTriviaMap.containsKey(name);
    }

    public static List<TriviaQuestion> getTriviaList() {
        return triviaList;
    }

    public static TriviaQuestion getTriviaQuestion() {
        return triviaQuestion;
    }

    public static void removeTriviaQuestion() {
        triviaQuestion = null;
    }
}