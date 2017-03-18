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

public class RunTrivia {

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
                .delay(1, TimeUnit.SECONDS)
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
        nextQuestion();
        Sponge.getServer().getBroadcastChannel().send(Text.of(TextColors.DARK_GRAY, "[", TextColors.DARK_PURPLE, "TriviaQuest", TextColors.DARK_GRAY, "] ",
                TextColors.WHITE, triviaQuestion.getQuestion()));
        triviaTask = Task.builder().execute(new TriviaTask())
                .name("triviaTask")
                .delay(0, TimeUnit.SECONDS)
                .interval(1, TimeUnit.SECONDS)
                .submit(TriviaQuest.getPlugin());
    }

    public static boolean checkAnswer(CommandSource src, String playerAnswer) {
        if (playerAnswer.equalsIgnoreCase(triviaQuestion.getAnswer())) {
            RunTrivia.endQuestion(src);
            if (src instanceof Player) {
                src.sendMessage(Text.of(TextColors.DARK_GRAY, "[", TextColors.DARK_PURPLE, "TriviaQuest", TextColors.DARK_GRAY, "] ",
                        TextColors.WHITE, "Nice job! Here's you reward!"));
                String triviaReward = Config.getTriviaReward();
                triviaReward = triviaReward.replaceAll("\\{player}", src.getName());
                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), triviaReward);
            } else {
                src.sendMessage(Text.of(TextColors.DARK_GRAY, "[", TextColors.DARK_PURPLE, "TriviaQuest", TextColors.DARK_GRAY, "] ",
                        TextColors.WHITE, "Aww, I can't give you a reward :("));
            }
            return true;
        }
        return false;
    }

    public static void endQuestion(CommandSource src) {
        String sourceName;
        if (src instanceof Player) {
            sourceName = src.getName();
        } else if (src instanceof ConsoleSource) {
            sourceName = ("The Overlord");
        } else {
            sourceName = ("A ninja");
        }

        triviaTask.cancel();
        removeTriviaQuestion();
        Sponge.getServer().getBroadcastChannel().send(Text.of(TextColors.DARK_GRAY, "[", TextColors.DARK_PURPLE, "TriviaQuest", TextColors.DARK_GRAY, "] ",
                TextColors.LIGHT_PURPLE, sourceName, TextColors.WHITE, " got it! The answer is ", TextColors.LIGHT_PURPLE, triviaQuestion.getAnswer()));
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
