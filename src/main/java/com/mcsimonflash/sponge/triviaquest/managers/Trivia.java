package com.mcsimonflash.sponge.triviaquest.managers;

import com.google.common.collect.Lists;

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
import java.util.concurrent.TimeUnit;

public class Trivia {

    private static Task runnerTask;
    private static Task triviaTask;
    private static int triviaIndex = 0;
    private static boolean triviaRunning = false;
    private static List<TriviaQuestion> triviaList = Lists.newArrayList();
    private static TriviaQuestion triviaQuestion;

    public static void startRunner() {
        triviaRunning = true;
        runnerTask = Task.builder().execute(
                task -> askQuestion())
                .name("triviaTask")
                .delay(0, TimeUnit.SECONDS)
                .interval(Config.getTriviaInterval(), TimeUnit.SECONDS)
                .submit(TriviaQuest.getPlugin());
    }

    public static void stopRunner() {
        runnerTask.cancel();
        triviaRunning = false;
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
        if (!isTriviaActive()) {
            nextQuestion();
            Sponge.getServer().getBroadcastChannel().send(Text.of(Config.getTriviaPrefix(),
                    TextColors.WHITE, triviaQuestion.getQuestion()));
            triviaTask = Task.builder().execute(new TriviaTask())
                    .name("triviaTask")
                    .delay(0, TimeUnit.SECONDS)
                    .interval(1, TimeUnit.SECONDS)
                    .submit(TriviaQuest.getPlugin());
        }
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
            String sourceName;
            Text rewardMsg;
            if (src instanceof Player) {
                sourceName = src.getName();
                rewardMsg = Text.of(Config.getTriviaPrefix(),
                        TextColors.WHITE, "Nice job! Here's your reward!");
                String rewardCmd = Config.getTriviaReward();
                if (rewardCmd != null) {
                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), rewardCmd.replaceAll("\\{player}", src.getName()));
                }
            } else if (src instanceof ConsoleSource){
                sourceName = ("GLaDOS");
                rewardMsg = Text.of(Config.getTriviaPrefix(),
                        TextColors.WHITE, "Aww, I can't give you a reward :(");
            } else {
                sourceName = ("The Companion Cube");
                rewardMsg = Text.of(TextColors.DARK_RED, "I will find you, and I will kill you.");
            }
            if (triviaQuestion.getAnswer().size() < 2) {
                Sponge.getServer().getBroadcastChannel().send(Text.of(Config.getTriviaPrefix(),
                        TextColors.LIGHT_PURPLE, sourceName, TextColors.WHITE, " got it! The answer is ", TextColors.LIGHT_PURPLE, triviaQuestion.getAnswer()));
            } else {
                Sponge.getServer().getBroadcastChannel().send(Text.of(Config.getTriviaPrefix(),
                        TextColors.LIGHT_PURPLE, sourceName, TextColors.WHITE, " got it! The answers are ", TextColors.LIGHT_PURPLE, String.join(", ", triviaQuestion.getAnswer()), "!"));
            }
            src.sendMessage(rewardMsg);
            Trivia.endQuestion();
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