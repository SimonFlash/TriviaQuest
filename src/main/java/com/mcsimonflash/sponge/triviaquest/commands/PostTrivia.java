package com.mcsimonflash.sponge.triviaquest.commands;

import com.mcsimonflash.sponge.triviaquest.managers.RunTrivia;

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
        if (RunTrivia.isTriviaActive()) {
            src.sendMessage(Text.of(TextColors.DARK_GRAY, "[", TextColors.DARK_PURPLE, "TriviaQuest", TextColors.DARK_GRAY, "] ",
                    TextColors.WHITE, "There's already a trivia running!"));
        } else {
            RunTrivia.askQuestion();
        }

        return CommandResult.success();
    }
}