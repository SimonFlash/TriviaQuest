package com.mcsimonflash.sponge.triviaquest.commands;

import com.mcsimonflash.sponge.triviaquest.managers.Trivia;

import com.mcsimonflash.sponge.triviaquest.managers.Util;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

public class PostTrivia implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (Trivia.trivia != null) {
            throw new CommandException(Util.toText("A trivia question is currently active!"));
        } else if (Trivia.runnerTask != null) {
            Trivia.runnerTask.cancel();
        }
        Trivia.askQuestion(true);
        return CommandResult.success();
    }
}