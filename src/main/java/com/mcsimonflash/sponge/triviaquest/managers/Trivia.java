package com.mcsimonflash.sponge.triviaquest.managers;

import com.google.common.collect.Lists;
import com.mcsimonflash.sponge.triviaquest.TriviaQuest;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Trivia {

    public static Text prefix;

    public static boolean runnerEnabled = false;
    public static int triviaIndex = 0;
    public static Task runnerTask;
    public static com.mcsimonflash.sponge.triviaquest.objects.Trivia trivia = null;
    public static List<com.mcsimonflash.sponge.triviaquest.objects.Trivia> triviaList = Lists.newArrayList();

    public static void startRunner() {
        if (runnerTask != null) {
            runnerTask.cancel();
        }
        runnerTask = Task.builder()
                .name("TriviaRunner")
                .execute(t -> {
                    askQuestion(false);
                })
                .delay(Config.triviaInterval, TimeUnit.SECONDS)
                .submit(TriviaQuest.getInstance());
    }

    public static void newQuestion() {
        if (triviaIndex == 0) {
            triviaIndex = triviaList.size();
            Collections.shuffle(triviaList);
        }
        trivia = triviaList.get(--triviaIndex);
    }

    public static void askQuestion(boolean override) {
        if (shouldTriviaRun(override) && trivia == null) {
            newQuestion();
            Sponge.getServer().getBroadcastChannel().send(prefix.concat(Util.toText(trivia.getQuestion())));
            runnerTask = Task.builder().execute(t -> {
                closeQuestion(false);
            }).name("Question").delay(Config.triviaLength, TimeUnit.SECONDS).submit(TriviaQuest.getInstance());
        } else if (runnerEnabled) {
            startRunner();
        }
    }

    public static void closeQuestion(boolean answered) {
        if (!answered) {
            Sponge.getServer().getBroadcastChannel().send(prefix.concat(Util.toText(Config.timesUp.replace("<word>", trivia.getAnswer()))));
        }
        trivia = null;
        if (runnerTask != null) {
            runnerTask.cancel();
        }
        if (runnerEnabled) {
            startRunner();
        }
    }

    public static boolean shouldTriviaRun(boolean override) {
        if (override) {
            return true;
        } else if (!runnerEnabled) {
            return false;
        }
        return Sponge.getServer().getOnlinePlayers().size() >= Config.enableTriviaCount;
    }

    public static boolean processAnswer(CommandSource src, String answer) {
        if (trivia.checkAnswer(answer)) {

            Sponge.getServer().getBroadcastChannel().send(prefix.concat(Util.toText(Config.answerCorrect.replace("<player>", src.getName()).replace("<answer>", trivia.getAnswer()))));
            if (Sponge.getServer().getOnlinePlayers().size() >= Config.enableRewardsCount) {
                String rewardCmd = Util.getReward().orElse(null);
                if (rewardCmd != null && !rewardCmd.isEmpty()) {
                    if (src instanceof Player) {
                        Sponge.getCommandManager().process(Sponge.getServer().getConsole(), rewardCmd.replace("<player>", src.getName()));
                    } else {
                        src.sendMessage(prefix.concat(Util.toText(Config.onlyPlayerCanReceive)));
                    }
                }
            }
            closeQuestion(true);
            return true;
        }
        return false;
    }
}