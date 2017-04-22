package com.mcsimonflash.sponge.triviaquest.commands;

import com.mcsimonflash.sponge.triviaquest.managers.Config;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class EnablePack implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String packName = args.<String>getOne("packName").get();

        if (!Config.packExists(packName)) {
            src.sendMessage(Text.of(Config.getTriviaPrefix(),
                    TextColors.WHITE, "The trivia pack ", TextColors.LIGHT_PURPLE, packName, TextColors.WHITE, " does not exist!"));
            return CommandResult.empty();
        }
        if (Config.isPackEnabled(packName)) {
            src.sendMessage(Text.of(Config.getTriviaPrefix(),
                    TextColors.WHITE, "The trivia pack ", TextColors.LIGHT_PURPLE, packName, TextColors.WHITE, " is already enabled!"));
            return CommandResult.empty();
        } else {
            Config.enablePack(packName);
            src.sendMessage(Text.of(Config.getTriviaPrefix(),
                    TextColors.WHITE, "Enabled the trivia pack ", TextColors.LIGHT_PURPLE, packName, TextColors.WHITE, "!"));
            src.sendMessage(Text.of(Config.getTriviaPrefix(),
                    TextColors.WHITE, "Use ", TextColors.LIGHT_PURPLE, "/TriviaQuest ReloadQuestions ", TextColors.WHITE, " to reload active questions!"));
            return CommandResult.success();
        }
    }
}
