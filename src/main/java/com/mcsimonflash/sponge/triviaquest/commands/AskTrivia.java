package com.mcsimonflash.sponge.triviaquest.commands;

import com.mcsimonflash.sponge.triviaquest.managers.Config;
import com.mcsimonflash.sponge.triviaquest.managers.Trivia;
import com.mcsimonflash.sponge.triviaquest.objects.TriviaQuestion;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;

public class AskTrivia implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String quesAnsStr = args.<String>getOne("quesAnsStr").get();

        if (Config.isPlayerQuestions()) {
            if (!Trivia.askedQuestion(src.getName())) {
                if (quesAnsStr.toLowerCase().contains(" ans:")) {
                    String question = quesAnsStr.substring(0, quesAnsStr.toLowerCase().indexOf(" ans:"));
                    String answer = quesAnsStr.substring(quesAnsStr.toLowerCase().indexOf(" ans:") + 5, quesAnsStr.length());
                    if (question.isEmpty()) {
                        src.sendMessage(Text.of(Config.getTriviaPrefix(),
                                TextColors.WHITE, "Your question was empty!"));
                        return CommandResult.empty();
                    } else if (answer.replaceAll(",", "").isEmpty()) {
                        src.sendMessage(Text.of(Config.getTriviaPrefix(),
                                TextColors.WHITE, "Your answer was empty!"));
                        return CommandResult.empty();
                    }
                    src.sendMessage(Text.of(Config.getTriviaPrefix(),
                            TextColors.WHITE, "Your question has been sent to the queue."));
                    TriviaQuestion trivia = new TriviaQuestion(question, Arrays.asList(answer.split(",")), true);
                    Trivia.registerQuestion(src.getName(), trivia);
                    return CommandResult.success();
                } else {
                    src.sendMessage(Text.of(Config.getTriviaPrefix(),
                            TextColors.WHITE, "Your question did not have an answer! (declare ans:<answer>)"));
                    return CommandResult.empty();
                }
            } else {
                src.sendMessage(Text.of(Config.getTriviaPrefix(),
                        TextColors.WHITE, "You already have a question in the queue!"));
                return CommandResult.empty();
            }
        } else {
            src.sendMessage(Text.of(Config.getTriviaPrefix(),
                    TextColors.WHITE, "Player questions are currently disabled!"));
            return CommandResult.empty();
        }
    }
}