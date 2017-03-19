package com.mcsimonflash.sponge.triviaquest.objects;

import com.mcsimonflash.sponge.triviaquest.managers.Config;
import com.mcsimonflash.sponge.triviaquest.managers.RunTrivia;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.function.Consumer;

public class TriviaTask implements Consumer<Task> {

    private int seconds = Config.getTriviaLength();

    @Override
    public void accept(Task task) {
        seconds--;
        if (seconds < 1) {
            Sponge.getServer().getBroadcastChannel().send(Text.of(Config.getTriviaPrefix(),
                    TextColors.WHITE, "No one answered in time! The answer is ", TextColors.LIGHT_PURPLE, RunTrivia.getTriviaQuestion().getAnswer(), "!"));
            task.cancel();
            RunTrivia.removeTriviaQuestion();
        }
    }
}