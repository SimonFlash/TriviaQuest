package com.mcsimonflash.sponge.triviaquest.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

public class Help implements CommandExecutor{

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        src.sendMessage(Text.of(TextColors.DARK_AQUA, "+=-=-=-=-=[TriviaQuest]=-=-=-=-=+"));
        if (src.hasPermission("triviaquest.answer")) {
            src.sendMessage(Text.builder("/Trivia AnswerTrivia ")
                    .color(TextColors.DARK_AQUA)
                    .onClick(TextActions.suggestCommand("/Trivia AnswerTrivia "))
                    .onHover(TextActions.showText(Text.of(
                            TextColors.DARK_AQUA, "AnswerTrivia: ", TextColors.AQUA, "Adds a task to the task list\n",
                            TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "AnswerTrivia, Answer, ans\n",
                            TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "triviaquest.answer\n",
                            TextColors.DARK_AQUA, "Note: ", TextColors.AQUA, "Players must have this permission to use integrated chat parsing")))
                    .append(Text.builder("<Answer> ")
                            .color(TextColors.AQUA)
                            .onHover(TextActions.showText(Text.of(
                                    TextColors.DARK_AQUA, "Answer<String>: ", TextColors.AQUA, "Answer of the trivia question")))
                            .build())
                    .build());
        }

        if (src.hasPermission("triviaquest.post")) {
            src.sendMessage(Text.builder("/Trivia PostTrivia")
                    .color(TextColors.DARK_AQUA)
                    .onClick(TextActions.suggestCommand("/Trivia PostTrivia"))
                    .onHover(TextActions.showText(Text.of(
                            TextColors.DARK_AQUA, "PostTrivia: ", TextColors.AQUA, "Automatically posts a random trivia\n",
                            TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "PostTrivia, Post\n",
                            TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "triviaquest.post")))
                    .build());
        }

        if (src.hasPermission("triviaquest.run.start")) {
            src.sendMessage(Text.builder("/Trivia StartTrivia")
                    .color(TextColors.DARK_AQUA)
                    .onClick(TextActions.suggestCommand("/Trivia StartTrivia"))
                    .onHover(TextActions.showText(Text.of(
                            TextColors.DARK_AQUA, "AnswerTrivia: ", TextColors.AQUA, "Starts automatic trivia posting\n",
                            TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "StartTrivia, Start, on\n",
                            TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "triviaquest.run.start")))
                    .build());
        }

        if (src.hasPermission("triviaquest.run.stop")) {
            if (src.hasPermission("triviaquest.run.stop"))
                src.sendMessage(Text.builder("/Trivia StopTrivia")
                        .color(TextColors.DARK_AQUA)
                        .onClick(TextActions.suggestCommand("/Trivia StopTrivia"))
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "AnswerTrivia: ", TextColors.AQUA, "Stops automatic trivia posting\n",
                                TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "StopTrivia, Stop, off\n",
                                TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "triviaquest.run.stop")))
                        .build());
        }

        return CommandResult.success();
    }
}
