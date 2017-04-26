package com.mcsimonflash.sponge.triviaquest.objects;

import com.mcsimonflash.sponge.triviaquest.managers.Config;
import com.mcsimonflash.sponge.triviaquest.managers.Trivia;

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
            Text broadcast = Text.of(Config.getTriviaPrefix(),
                    TextColors.WHITE, "No one answered fast enough! ");
            if (Config.isShowAnswer()) {
                if (Trivia.getTriviaQuestion().getAnswer().size() == 1) {
                    broadcast = broadcast.concat(Text.of("The answer was ", TextColors.LIGHT_PURPLE, Trivia.getTriviaQuestion().getAnswer(), "!"));
                } else {
                    broadcast = broadcast.concat(Text.of("The answers were ", TextColors.LIGHT_PURPLE, String.join(", ", Trivia.getTriviaQuestion().getAnswer()), "!"));
                }
            } else {
                broadcast = broadcast.concat(Text.of("Better luck next time!"));
            }
            Sponge.getServer().getBroadcastChannel().send(broadcast);
            task.cancel();
            Trivia.removeTriviaQuestion();
        }
    }
}