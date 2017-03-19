package com.mcsimonflash.sponge.triviaquest;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import com.mcsimonflash.sponge.triviaquest.commands.*;
import com.mcsimonflash.sponge.triviaquest.managers.Config;
import com.mcsimonflash.sponge.triviaquest.managers.RunTrivia;

import org.slf4j.Logger;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.nio.file.Path;

@Plugin(id = "triviaquest", name = "TriviaQuest", version = "mc1.10.2-v1.1.1", description = "In-Game Trivia Questions - Developed by Simon_Flash")
public class TriviaQuest {

    private static TriviaQuest plugin;
    public static TriviaQuest getPlugin() {
        return plugin;
    }

    @Inject
    private Logger logger;
    public Logger getLogger() {
        return logger;
    }

    @Inject
    @DefaultConfig(sharedRoot = true)
    private Path defaultConfig;
    public Path getDefaultConfig() {
        return defaultConfig;
    }

    @Listener
    public void onInitilization(GameInitializationEvent event) {
        plugin = this;

        getLogger().info("+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
        getLogger().info("|     TriviaQuest - Version 1.1.1     |");
        getLogger().info("|      Developed by: Simon_Flash      |");
        getLogger().info("+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");

        CommandSpec cmdSpec_AnswerTrivia = CommandSpec.builder()
                .executor(new AnswerTrivia())
                .description(Text.of("Answer a TriviaQuest question"))
                .permission("triviaquest.answer")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.remainingJoinedStrings(Text.of("playerAnswer"))))
                .build();
        CommandSpec cmdSpec_PostTrivia = CommandSpec.builder()
                .executor(new PostTrivia())
                .description(Text.of("Posts TriviaQuest question"))
                .permission("triviaquest.post")
                .build();
        CommandSpec cmdSpec_StartTriva = CommandSpec.builder()
                .executor(new StartTrivia())
                .description(Text.of("Starts automatic TriviaQuest trivia"))
                .permission("triviaquest.run.start")
                .build();
        CommandSpec cmdSpec_StopTrivia = CommandSpec.builder()
                .executor(new StopTrivia())
                .description(Text.of("Stops automatic TriviaQuest trivia"))
                .permission("triviaquest.run.stop")
                .build();
        CommandSpec cmdSpec_TriviaQuest = CommandSpec.builder()
                .executor(new Help())
                .description(Text.of("Opens in-game documentation"))
                .permission("triviaquest")
                .child(cmdSpec_AnswerTrivia, "AnswerTrivia", "Answer", "ans")
                .child(cmdSpec_PostTrivia, "PostTrivia", "Post")
                .child(cmdSpec_StartTriva, "StartTrivia", "Start", "on")
                .child(cmdSpec_StopTrivia, "StopTrivia", "Stop", "off")
                .build();
        Sponge.getCommandManager().register(this, cmdSpec_TriviaQuest, Lists.newArrayList("TriviaQuest", "Trivia", "tq"));

        Config.readConfig();
    }

    @Listener
    public void onMessageSend(MessageChannelEvent.Chat event, @First Player player) {
        if (RunTrivia.isTriviaActive() && player.hasPermission("triviaquest.answer")) {
            if (RunTrivia.checkAnswer(player, event.getRawMessage().toPlain())) {
                event.setMessageCancelled(true);
            }
        }
    }

    @Listener
    public void onReload(GameReloadEvent event) {
        Config.readConfig();
    }
}