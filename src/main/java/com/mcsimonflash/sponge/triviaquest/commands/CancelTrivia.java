package com.mcsimonflash.sponge.triviaquest.commands;

import com.mcsimonflash.sponge.triviaquest.managers.Config;
import com.mcsimonflash.sponge.triviaquest.managers.Trivia;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class CancelTrivia implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = args.<Player>getOne("opt-player").isPresent() ? args.<Player>getOne("opt-player").get() : null;

        if (player == null) {
            if (src instanceof Player) {
                player = (Player) src;
            } else {
                src.sendMessage(Text.of(Config.getTriviaPrefix(),
                        TextColors.WHITE, "A player must be defined to use this command!"));
                return CommandResult.empty();
            }
        }
        if (src.hasPermission("triviaquest.cancel.other") || player.equals(src)) {
            if (Trivia.askedQuestion(player.getName())) {
                Trivia.unregisterQuestion(player.getName());
                return CommandResult.success();
            } else {
                src.sendMessage(Text.of(Config.getTriviaPrefix(),
                        TextColors.LIGHT_PURPLE, player.getName() + TextColors.WHITE, " has not asked a question!"));
                return CommandResult.empty();
            }
        } else {
            src.sendMessage(Text.of(Config.getTriviaPrefix(),
                    TextColors.WHITE, "You do not have permission to cancel another's question!"));
            return CommandResult.empty();
        }
    }
}
