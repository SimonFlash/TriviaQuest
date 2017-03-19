package com.mcsimonflash.sponge.triviaquest.commands;

import com.mcsimonflash.sponge.triviaquest.managers.Config;
import com.mcsimonflash.sponge.triviaquest.managers.RunTrivia;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class AnswerTrivia implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String playerAnswer = args.<String>getOne("playerAnswer").get();

        if (RunTrivia.isTriviaActive()) {
            src.sendMessage(Text.of(Config.getTriviaPrefix(),
                    TextColors.WHITE, "Oh no! There isn't an active question!"));
            return CommandResult.empty();
        } else {
            RunTrivia.checkAnswer(src, playerAnswer);
            return CommandResult.success();
        }
    }
}