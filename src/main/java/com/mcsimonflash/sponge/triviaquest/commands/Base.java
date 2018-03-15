package com.mcsimonflash.sponge.triviaquest.commands;

import com.mcsimonflash.sponge.triviaquest.TriviaQuest;
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
    public CommandResult execute(CommandSource src, CommandContext args) {
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
                                TextColors.DARK_PURPLE, "Subcommands: ", TextColors.LIGHT_PURPLE, "AnswerTrivia, AskTrivia, CancelTrivia, DisablePack, EnablePack, PostTrivia, ReloadTrivia, ToggleRunner, StopTrivia")))
                        .build())
                .build());
        if (src.hasPermission("triviaquest.answertrivia.base")) {
            src.sendMessage(Text.builder("/TriviaQuest AnswerTrivia ")
                    .color(TextColors.DARK_PURPLE)
                    .onClick(TextActions.suggestCommand("/TriviaQuest AnswerTrivia "))
                    .onHover(TextActions.showText(Text.of(
                            TextColors.DARK_PURPLE, "AnswerTrivia: ", TextColors.LIGHT_PURPLE, "Answers a trivia question\n",
                            TextColors.DARK_PURPLE, "Aliases: ", TextColors.LIGHT_PURPLE, "AnswerTrivia, Answer, ans\n",
                            TextColors.DARK_PURPLE, "Permission: ", TextColors.LIGHT_PURPLE, "triviaquest.answertrivia.base\n",
                            TextColors.DARK_PURPLE, "Note: ", TextColors.LIGHT_PURPLE, "Players must have triviaquest.answertrivia.chat to use chat parsing")))
                    .append(Text.builder("<Answer>")
                            .color(TextColors.LIGHT_PURPLE)
                            .onHover(TextActions.showText(Text.of(
                                    TextColors.DARK_PURPLE, "Answer<String>: ", TextColors.LIGHT_PURPLE, "Answer of the trivia question")))
                            .build())
                    .build());
        }
        if (src.hasPermission("triviaquest.posttrivia.base")) {
            src.sendMessage(Text.builder("/TriviaQuest PostTrivia")
                    .color(TextColors.DARK_PURPLE)
                    .onClick(TextActions.suggestCommand("/TriviaQuest PostTrivia"))
                    .onHover(TextActions.showText(Text.of(
                            TextColors.DARK_PURPLE, "PostTrivia: ", TextColors.LIGHT_PURPLE, "Posts a trivia question from the pool\n",
                            TextColors.DARK_PURPLE, "Aliases: ", TextColors.LIGHT_PURPLE, "PostTrivia, Post, pt\n",
                            TextColors.DARK_PURPLE, "Permission: ", TextColors.LIGHT_PURPLE, "triviaquest.posttrivia.base")))
                    .build());
        }
        if (src.hasPermission("triviaquest.togglerunner.base")) {
            src.sendMessage(Text.builder("/TriviaQuest ToggleRunner")
                    .color(TextColors.DARK_PURPLE)
                    .onClick(TextActions.suggestCommand("/TriviaQuest ToggleRunner"))
                    .onHover(TextActions.showText(Text.of(
                            TextColors.DARK_PURPLE, "ToggleRunner: ", TextColors.LIGHT_PURPLE, "Enables/Disables the integrated trivia runner\n",
                            TextColors.DARK_PURPLE, "Aliases: ", TextColors.LIGHT_PURPLE, "StopTrivia, Stop, off\n",
                            TextColors.DARK_PURPLE, "Permission: ", TextColors.LIGHT_PURPLE, "triviaquest.togglerunner.base")))
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
