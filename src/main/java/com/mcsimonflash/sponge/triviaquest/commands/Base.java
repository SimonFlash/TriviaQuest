package com.mcsimonflash.sponge.triviaquest.commands;

import com.mcsimonflash.sponge.triviaquest.TriviaQuest;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

public class Base implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        src.sendMessage(Text.of(TextColors.DARK_PURPLE, "+=-=-=-=-=[", TextColors.LIGHT_PURPLE, "TriviaQuest", TextColors.DARK_PURPLE, "]=-=-=-=-=+"));
        src.sendMessage(Text.builder("/TriviaQuest ")
                .color(TextColors.DARK_PURPLE)
                .onClick(TextActions.suggestCommand("/TriviaQuest "))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_PURPLE, "TriviaQuest: ", TextColors.LIGHT_PURPLE, "Opens command reference menu",
                        TextColors.DARK_PURPLE, "Aliases: ", TextColors.LIGHT_PURPLE, "/TriviaQuest, /Trivia, /tq\n",
                        TextColors.DARK_PURPLE, "Permission: ", TextColors.LIGHT_PURPLE, "triviaquest.base\n",
                        TextColors.DARK_PURPLE, "Note: ", TextColors.LIGHT_PURPLE, "Players must have this permission to use any TriviaQuest command")))
                .append(Text.builder("<Subcommand>")
                        .color(TextColors.LIGHT_PURPLE)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_PURPLE, "Subcommands: ", TextColors.LIGHT_PURPLE, "AnswerTrivia, AskTrivia, CancelTrivia, DisablePack, EnablePack, PostTrivia, ReloadTrivia, StartTrivia, StopTrivia")))
                        .build())
                .build());
        if (src.hasPermission("triviaquest.answer.command")) {
            src.sendMessage(Text.builder("/TriviaQuest AnswerTrivia ")
                    .color(TextColors.DARK_PURPLE)
                    .onClick(TextActions.suggestCommand("/TriviaQuest AnswerTrivia "))
                    .onHover(TextActions.showText(Text.of(
                            TextColors.DARK_PURPLE, "AnswerTrivia: ", TextColors.LIGHT_PURPLE, "Answers a trivia question\n",
                            TextColors.DARK_PURPLE, "Aliases: ", TextColors.LIGHT_PURPLE, "AnswerTrivia, Answer, ans\n",
                            TextColors.DARK_PURPLE, "Permission: ", TextColors.LIGHT_PURPLE, "triviaquest.answer.command\n",
                            TextColors.DARK_PURPLE, "Note: ", TextColors.LIGHT_PURPLE, "Players must have triviaquest.answer.chat to use chat parsing")))
                    .append(Text.builder("<Answer>")
                            .color(TextColors.LIGHT_PURPLE)
                            .onHover(TextActions.showText(Text.of(
                                    TextColors.DARK_PURPLE, "Answer<String>: ", TextColors.LIGHT_PURPLE, "Answer of the trivia question")))
                            .build())
                    .build());
        }
        if (src.hasPermission("triviaquest.ask")) {
            src.sendMessage(Text.builder("/TriviaQuest AskTrivia ")
                    .color(TextColors.DARK_PURPLE)
                    .onClick(TextActions.suggestCommand("/TriviaQuest AskTrivia "))
                    .onHover(TextActions.showText(Text.of(
                            TextColors.DARK_PURPLE, "AskTrivia: ", TextColors.LIGHT_PURPLE, "Asks a trivia question\n",
                            TextColors.DARK_PURPLE, "Aliases: ", TextColors.LIGHT_PURPLE, "AskTrivia, ask\n",
                            TextColors.DARK_PURPLE, "Permission: ", TextColors.LIGHT_PURPLE, "triviaquest.ask\n")))
                    .append(Text.builder("<Trivia>")
                            .color(TextColors.LIGHT_PURPLE)
                            .onHover(TextActions.showText(Text.of(
                                    TextColors.DARK_PURPLE, "Trivia<String>: ", TextColors.LIGHT_PURPLE, "New trivia question. Uses the format [<Question> ans:<Answer>")))
                            .build())
                    .build());
        }
        if (src.hasPermission("triviaquest.cancel.base")) {
            if (src.hasPermission("triviaquest.cancel.other")) {
                src.sendMessage(Text.builder("/TriviaQuest CancelTrivia ")
                        .color(TextColors.DARK_PURPLE)
                        .onClick(TextActions.suggestCommand("/TriviaQuest CancelTrivia "))
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_PURPLE, "AskTrivia: ", TextColors.LIGHT_PURPLE, "Cancels a player's queued question\n",
                                TextColors.DARK_PURPLE, "Aliases: ", TextColors.LIGHT_PURPLE, "CancelTrivia, Cancel, can\n",
                                TextColors.DARK_PURPLE, "Permission: ", TextColors.LIGHT_PURPLE, "triviaquest.cancel.base\n")))
                        .append(Text.builder("<Opt-Player>")
                                .color(TextColors.LIGHT_PURPLE)
                                .onHover(TextActions.showText(Text.of(
                                        TextColors.DARK_PURPLE, "Player<Player>: ", TextColors.LIGHT_PURPLE, "Player to cancel question, else yourself")))
                                .build())
                        .build());
            } else {
                src.sendMessage(Text.builder("/TriviaQuest CancelTrivia ")
                        .color(TextColors.DARK_PURPLE)
                        .onClick(TextActions.suggestCommand("/TriviaQuest CancelTrivia "))
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_PURPLE, "AskTrivia: ", TextColors.LIGHT_PURPLE, "Cancels your queued question\n",
                                TextColors.DARK_PURPLE, "Aliases: ", TextColors.LIGHT_PURPLE, "CancelTrivia, Cancel, can\n",
                                TextColors.DARK_PURPLE, "Permission: ", TextColors.LIGHT_PURPLE, "triviaquest.cancel.base\n")))
                        .build());
            }
        }
        if (src.hasPermission("triviaquest.packs.disable")) {
            src.sendMessage(Text.builder("/Trivia DisablePack ")
                    .color(TextColors.DARK_PURPLE)
                    .onClick(TextActions.suggestCommand("/Trivia DisablePack "))
                    .onHover(TextActions.showText(Text.of(
                            TextColors.DARK_PURPLE, "DisablePack: ", TextColors.LIGHT_PURPLE, "Disables a trivia pack\n",
                            TextColors.DARK_PURPLE, "Aliases: ", TextColors.LIGHT_PURPLE, "DisablePack, Disable, dp\n",
                            TextColors.DARK_PURPLE, "Permission: ", TextColors.LIGHT_PURPLE, "triviaquest.packs.disable\n",
                            TextColors.DARK_PURPLE, "Note: ", TextColors.LIGHT_PURPLE, "Trivia pool must be reloaded to see changes")))
                    .append(Text.builder("<Pack>")
                            .color(TextColors.LIGHT_PURPLE)
                            .onHover(TextActions.showText(Text.of(
                                    TextColors.DARK_PURPLE, "Pack<String>: ", TextColors.LIGHT_PURPLE, "Name of the trivia pack")))
                            .build())
                    .build());
        }
        if (src.hasPermission("triviaquest.packs.enable")) {
            src.sendMessage(Text.builder("/Trivia EnablePack ")
                    .color(TextColors.DARK_PURPLE)
                    .onClick(TextActions.suggestCommand("/Trivia EnablePack "))
                    .onHover(TextActions.showText(Text.of(
                            TextColors.DARK_PURPLE, "EnablePack: ", TextColors.LIGHT_PURPLE, "Enables a trivia pack\n",
                            TextColors.DARK_PURPLE, "Aliases: ", TextColors.LIGHT_PURPLE, "EnablePack, Enable, ep\n",
                            TextColors.DARK_PURPLE, "Permission: ", TextColors.LIGHT_PURPLE, "triviaquest.packs.enable\n",
                            TextColors.DARK_PURPLE, "Note: ", TextColors.LIGHT_PURPLE, "Trivia pool must be reloaded to see changes")))
                    .append(Text.builder("<Pack>")
                            .color(TextColors.LIGHT_PURPLE)
                            .onHover(TextActions.showText(Text.of(
                                    TextColors.DARK_PURPLE, "Pack<String>: ", TextColors.LIGHT_PURPLE, "Name of the trivia pack")))
                            .build())
                    .build());
        }
        if (src.hasPermission("triviaquest.trivia.post")) {
            src.sendMessage(Text.builder("/Trivia PostTrivia")
                    .color(TextColors.DARK_PURPLE)
                    .onClick(TextActions.suggestCommand("/Trivia PostTrivia"))
                    .onHover(TextActions.showText(Text.of(
                            TextColors.DARK_PURPLE, "PostTrivia: ", TextColors.LIGHT_PURPLE, "Posts a trivia question from the pool\n",
                            TextColors.DARK_PURPLE, "Aliases: ", TextColors.LIGHT_PURPLE, "PostTrivia, Post, pt\n",
                            TextColors.DARK_PURPLE, "Permission: ", TextColors.LIGHT_PURPLE, "triviaquest.trivia.post")))
                    .build());
        }
        if (src.hasPermission("triviaquest.trivia.reload")) {
            src.sendMessage(Text.builder("/Trivia ReloadTrivia")
                    .color(TextColors.DARK_PURPLE)
                    .onClick(TextActions.suggestCommand("/Trivia ReloadTrivia"))
                    .onHover(TextActions.showText(Text.of(
                            TextColors.DARK_PURPLE, "ReloadTrivia: ", TextColors.LIGHT_PURPLE, "Reloads the trivia pool from the config\n",
                            TextColors.DARK_PURPLE, "Aliases: ", TextColors.LIGHT_PURPLE, "ReloadTrivia, Reload, rt\n",
                            TextColors.DARK_PURPLE, "Permission: ", TextColors.LIGHT_PURPLE, "triviaquest.trivia.reload")))
                    .build());
        }
        if (src.hasPermission("triviaquest.run.start")) {
            src.sendMessage(Text.builder("/Trivia StartTrivia")
                    .color(TextColors.DARK_PURPLE)
                    .onClick(TextActions.suggestCommand("/Trivia StartTrivia"))
                    .onHover(TextActions.showText(Text.of(
                            TextColors.DARK_PURPLE, "StartTrivia: ", TextColors.LIGHT_PURPLE, "Starts automatic trivia posting\n",
                            TextColors.DARK_PURPLE, "Aliases: ", TextColors.LIGHT_PURPLE, "StartTrivia, Start, on\n",
                            TextColors.DARK_PURPLE, "Permission: ", TextColors.LIGHT_PURPLE, "triviaquest.run.start")))
                    .build());
        }
        if (src.hasPermission("triviaquest.run.stop")) {
            src.sendMessage(Text.builder("/Trivia StopTrivia")
                    .color(TextColors.DARK_PURPLE)
                    .onClick(TextActions.suggestCommand("/Trivia StopTrivia"))
                    .onHover(TextActions.showText(Text.of(
                            TextColors.DARK_PURPLE, "StopTrivia: ", TextColors.LIGHT_PURPLE, "Stops automatic trivia posting\n",
                            TextColors.DARK_PURPLE, "Aliases: ", TextColors.LIGHT_PURPLE, "StopTrivia, Stop, off\n",
                            TextColors.DARK_PURPLE, "Permission: ", TextColors.LIGHT_PURPLE, "triviaquest.run.stop")))
                    .build());
        }
        if (TriviaQuest.getWiki() != null && TriviaQuest.getDiscord() != null) {
            src.sendMessage(Text.builder("| ")
                    .color(TextColors.DARK_PURPLE)
                    .append(Text.builder("TriviaQuest Wiki")
                            .color(TextColors.LIGHT_PURPLE).style(TextStyles.UNDERLINE)
                            .onClick(TextActions.openUrl(TriviaQuest.getWiki()))
                            .onHover(TextActions.showText(Text.of("Click to open the TriviaQuest Wiki")))
                            .build())
                    .append(Text.of(TextColors.DARK_PURPLE, " | "))
                    .append(Text.builder("Support Discord")
                            .color(TextColors.LIGHT_PURPLE).style(TextStyles.UNDERLINE)
                            .onClick(TextActions.openUrl(TriviaQuest.getDiscord()))
                            .onHover(TextActions.showText(Text.of("Click to open the Support Discord")))
                            .build())
                    .append(Text.of(TextColors.DARK_PURPLE, " |"))
                    .build());
        }
        return CommandResult.success();
    }
}
