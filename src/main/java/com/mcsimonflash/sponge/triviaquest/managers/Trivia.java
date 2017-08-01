package com.mcsimonflash.sponge.triviaquest.managers;

import com.google.common.collect.Lists;

import com.mcsimonflash.sponge.triviaquest.TriviaQuest;
import com.mcsimonflash.sponge.triviaquest.objects.ITrivia;
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
    public static ITrivia trivia;
    public static List<ITrivia> triviaList = Lists.newArrayList();

    public static void startRunner() {
        runnerTask = Task.builder().execute(t -> askQuestion(false)).name("TriviaRunner").delay(Config.triviaInterval, TimeUnit.SECONDS).submit(TriviaQuest.getPlugin());
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
            Sponge.getServer().getBroadcastChannel().send(Trivia.prefix.concat(Util.toText(trivia.getQuestion())));
            runnerTask = Task.builder().execute(t -> closeQuestion(false)).name("Question").delay(Config.triviaLength, TimeUnit.SECONDS).submit(TriviaQuest.getPlugin());
        } else {
            startRunner();
        }
    }

    public static void closeQuestion(boolean answered) {
        if (answered) {
            runnerTask.cancel();
        } else {
            Sponge.getServer().getBroadcastChannel().send(Trivia.prefix.concat(Util.toText("Times up! Better luck next time!")));
        }
        trivia = null;
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
            Sponge.getServer().getBroadcastChannel().send(Trivia.prefix.concat(Util.toText("&d" + src.getName() + "&f got it! Better luck next time! ^^")));
            if (Sponge.getServer().getOnlinePlayers().size() >= Config.enableRewardsCount) {
                String rewardCmd = Util.getReward();
                if (!rewardCmd.isEmpty()) {
                    if (src instanceof Player) {
                        Sponge.getCommandManager().process(Sponge.getServer().getConsole(), rewardCmd.replace("<player>", src.getName()));
                    } else {
                        src.sendMessage(Trivia.prefix.concat(Util.toText("Sorry! Only a player can receive a reward!")));
                    }
                }
            }
            closeQuestion(true);
            return true;
        }
        return false;
    }
}