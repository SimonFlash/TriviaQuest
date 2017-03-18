package com.mcsimonflash.sponge.triviaquest.commands;

import com.mcsimonflash.sponge.triviaquest.managers.RunTrivia;
import com.mcsimonflash.sponge.triviaquest.managers.Config;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class StartTrivia implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (RunTrivia.isRunnerActive()) {
            src.sendMessage(Text.of(TextColors.DARK_GRAY, "[", TextColors.DARK_PURPLE, "TriviaQuest", TextColors.DARK_GRAY, "] ",
                    TextColors.WHITE, "Integrated runner is already active!"));
        } else {
            RunTrivia.startRunner();
            src.sendMessage(Text.of(TextColors.DARK_GRAY, "[", TextColors.DARK_PURPLE, "TriviaQuest", TextColors.DARK_GRAY, "] ",
                    TextColors.WHITE, "Integrated runner activated! Running every ",
                    TextColors.LIGHT_PURPLE, Config.getTriviaInterval(), TextColors.WHITE, " seconds!"));
            if (!Sponge.getPluginManager().isLoaded("cmdcalendar")) {
                src.sendMessage(Text.of(TextColors.YELLOW, "[", TextColors.GOLD, "Simon_Flash", TextColors.YELLOW, "] ",
                        TextColors.WHITE, "Integrated runner activated! Running every ",
                        TextColors.LIGHT_PURPLE, Config.getTriviaInterval(), TextColors.WHITE, " seconds!"));
            } else {
                src.sendMessage(Text.of(TextColors.YELLOW, "Go CmdCalendar, Go!"));
            }
        }

        return CommandResult.success();
    }
}
