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
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.net.MalformedURLException;
import java.net.URL;

public class StartTrivia implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (RunTrivia.isRunnerActive()) {
            src.sendMessage(Text.of(Config.getTriviaPrefix(),
                    TextColors.WHITE, "Integrated runner is already active!"));
            return CommandResult.empty();
        } else {

            RunTrivia.startRunner();
            src.sendMessage(Text.of(Config.getTriviaPrefix(),
                    TextColors.WHITE, "Integrated runner activated! Running every ",
                    TextColors.LIGHT_PURPLE, Config.getTriviaInterval(), TextColors.WHITE, " seconds!"));

            if (!Sponge.getPluginManager().isLoaded("cmdcalendar")) {
                try {
                    src.sendMessage(Text.builder()
                            .append(Text.of(TextColors.YELLOW, "[", TextColors.DARK_RED, "Simon_Flash", TextColors.YELLOW, "] ",
                                    TextColors.WHITE, "Want more control over TriviaQuest? Download "))
                            .append(Text.builder("CmdCalendar")
                                    .color(TextColors.DARK_AQUA)
                                    .onClick(TextActions.openUrl(new URL("https://forums.spongepowered.org/t/cmdcalendar-calendar-automatic-command-scheduler-wip-beta/17735")))
                                    .onHover(TextActions.showText(Text.of(TextColors.YELLOW, "Click to visit the CmdCalendar page!")))
                                    .build())
                            .build());
                } catch (MalformedURLException ignored) {
                    //Exception ignored in event of URL change
                }
            }
            return CommandResult.success();
        }
    }
}
