package com.mcsimonflash.sponge.triviaquest.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

public class Base implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        src.sendMessage(Text.of(TextColors.DARK_AQUA, "+=-=-=-=-=[TriviaQuest]=-=-=-=-=+"));
        src.sendMessage(Text.builder("/TriviaQuest ")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/TriviaQuest"))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "TriviaQuest: ", TextColors.AQUA, "Opens command reference menu",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "/TriviaQuest, /Trivia, /tq\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "triviaquest.base\n",
                        TextColors.DARK_AQUA, "Note: ", TextColors.AQUA, "Players must have this permission to use any TriviaQuest command")))
                .append(Text.builder("<Subcommand> ")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Subcommands: ", TextColors.AQUA, "AnswerTrivia, DisablePack, EnablePack, PostTrivia, ReloadTrivia, StartTrivia, StopTrivia")))
                        .build())
                .build());
        src.sendMessage(Text.builder("/TriviaQuest AnswerTrivia ")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/TriviaQuest AnswerTrivia "))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "AnswerTrivia: ", TextColors.AQUA, "Answers a trivia question\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "AnswerTrivia, Answer, ans\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "triviaquest.answer.command\n",
                        TextColors.DARK_AQUA, "Note: ", TextColors.AQUA, "Players must have triviaquest.answer.chat to use chat parsing")))
                .append(Text.builder("<Answer> ")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Answer<String>: ", TextColors.AQUA, "Answer of the trivia question")))
                        .build())
                .build());
        src.sendMessage(Text.builder("/Trivia DisablePack")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/Trivia DisablePack"))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "DisablePack: ", TextColors.AQUA, "Disables a trivia pack\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "DisablePack, Disable, dp\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "triviaquest.packs.disable\n",
                        TextColors.DARK_AQUA, "Note: ", TextColors.AQUA, "Trivia pool must be reloaded to see changes")))
                .append(Text.builder("<Pack> ")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Pack<String>: ", TextColors.AQUA, "Name of the trivia pack")))
                        .build())
                .build());
        src.sendMessage(Text.builder("/Trivia EnablePack")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/Trivia EnablePack"))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "EnablePack: ", TextColors.AQUA, "Enables a trivia pack\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "EnablePack, Enable, ep\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "triviaquest.packs.enable\n",
                        TextColors.DARK_AQUA, "Note: ", TextColors.AQUA, "Trivia pool must be reloaded to see changes")))
                .append(Text.builder("<Pack> ")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Pack<String>: ", TextColors.AQUA, "Name of the trivia pack")))
                        .build())
                .build());
        src.sendMessage(Text.builder("/Trivia PostTrivia")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/Trivia PostTrivia"))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "PostTrivia: ", TextColors.AQUA, "Posts a trivia question from the pool\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "PostTrivia, Post, pt\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "triviaquest.trivia.post")))
                .build());
        src.sendMessage(Text.builder("/Trivia ReloadTrivia")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/Trivia ReloadTrivia"))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "ReloadTrivia: ", TextColors.AQUA, "Reloads the trivia pool from the config\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "ReloadTrivia, Reload, rt\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "triviaquest.trivia.reload")))
                .build());
        src.sendMessage(Text.builder("/Trivia StartTrivia")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/Trivia StartTrivia"))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "StartTrivia: ", TextColors.AQUA, "Starts automatic trivia posting\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "StartTrivia, Start, on\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "triviaquest.run.start")))
                .build());
        if (src.hasPermission("triviaquest.run.stop"))
            src.sendMessage(Text.builder("/Trivia StopTrivia")
                    .color(TextColors.DARK_AQUA)
                    .onClick(TextActions.suggestCommand("/Trivia StopTrivia"))
                    .onHover(TextActions.showText(Text.of(
                            TextColors.DARK_AQUA, "StopTrivia: ", TextColors.AQUA, "Stops automatic trivia posting\n",
                            TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "StopTrivia, Stop, off\n",
                            TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "triviaquest.run.stop")))
                    .build());
        return CommandResult.success();
    }
}
