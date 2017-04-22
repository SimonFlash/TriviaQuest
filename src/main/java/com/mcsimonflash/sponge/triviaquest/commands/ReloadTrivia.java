package com.mcsimonflash.sponge.triviaquest.commands;

import com.mcsimonflash.sponge.triviaquest.managers.Config;
import com.mcsimonflash.sponge.triviaquest.managers.Trivia;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class ReloadTrivia implements CommandExecutor{

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (Trivia.isTriviaActive()) {
            Sponge.getServer().getBroadcastChannel().send(Text.of(Config.getTriviaPrefix(),
                    TextColors.WHITE, "Questions are being reloaded! The answer was ", TextColors.LIGHT_PURPLE, Trivia.getTriviaQuestion().getAnswer(), "!"));
            Trivia.endQuestion();
        }
        Config.loadQuestions();
        src.sendMessage(Text.of(Config.getTriviaPrefix(),
                TextColors.WHITE, "Questions successfully reloaded!"));
        return CommandResult.success();
    }
}
