package com.mcsimonflash.sponge.triviaquest.commands;

import com.mcsimonflash.sponge.triviaquest.managers.Config;
import com.mcsimonflash.sponge.triviaquest.managers.Trivia;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class PostTrivia implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (Trivia.isTriviaActive()) {
            src.sendMessage(Text.of(Config.getTriviaPrefix(),
                    TextColors.WHITE, "There's already a trivia running!"));
            return CommandResult.empty();
        } else {
            Trivia.askQuestion();
            return CommandResult.success();
        }
    }
}