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

public class StopTrivia implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (RunTrivia.isRunnerActive()) {
            RunTrivia.stopRunner();
            src.sendMessage(Text.of(Config.getTriviaPrefix(),
                    TextColors.WHITE, "Integrated runner halted!"));
            return CommandResult.success();
        } else {
            src.sendMessage(Text.of(Config.getTriviaPrefix(),
                    TextColors.WHITE, "Integrated runner is not active!"));
            return CommandResult.empty();
        }
    }
}