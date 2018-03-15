package com.mcsimonflash.sponge.triviaquest.commands;

import com.mcsimonflash.sponge.triviaquest.managers.Trivia;

import com.mcsimonflash.sponge.triviaquest.managers.Util;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

public class AnswerTrivia implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (Trivia.trivia == null) {
            throw new CommandException(Util.toText("Oh no! There isn't an active trivia question!"));
        } else if (!Trivia.processAnswer(src, args.<String>getOne("answer").get())) {
            src.sendMessage(Trivia.prefix.concat(Util.toText("Oh no! That wasn't the answer :(")));
        }
        return CommandResult.success();
    }

}