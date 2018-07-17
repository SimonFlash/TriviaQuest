package com.mcsimonflash.sponge.triviaquest.commands;

import com.mcsimonflash.sponge.triviaquest.managers.Trivia;

import com.mcsimonflash.sponge.triviaquest.managers.Util;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

public class ToggleRunner implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (Trivia.runnerEnabled = !Trivia.runnerEnabled) {
            Trivia.startRunner();
        }
        src.sendMessage(Trivia.prefix.concat(Util.toText("The trivia runner has been &d" + (Trivia.runnerEnabled ? "enabled" : "disabled") + "&f!")));
        return CommandResult.success();
    }

}