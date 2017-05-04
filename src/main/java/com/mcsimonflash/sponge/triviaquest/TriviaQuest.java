package com.mcsimonflash.sponge.triviaquest;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import com.mcsimonflash.sponge.triviaquest.commands.*;
import com.mcsimonflash.sponge.triviaquest.managers.Config;
import com.mcsimonflash.sponge.triviaquest.managers.Trivia;

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

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

@Plugin(id = "triviaquest", name = "TriviaQuest", version = "mc1.10.2-v1.2.2", description = "In-Game Trivia Questions - Developed by Simon_Flash")
public class TriviaQuest {

    private static TriviaQuest plugin;
    public static TriviaQuest getPlugin() {
        return plugin;
    }

    private static URL wiki;
    public static URL getWiki() {
        return wiki;
    }

    private static URL discord;
    public static URL getDiscord() {
        return discord;
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
        getLogger().info("|     TriviaQuest - Version 1.2.2     |");
        getLogger().info("|      Developed By: Simon_Flash      |");
        getLogger().info("+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
        Config.readConfig();
        try {
            wiki = new URL("https://github.com/SimonFlash/TriviaQuest/wiki");
        } catch (MalformedURLException ignored) {
            getLogger().error("Unable to locate CmdCalendar Wiki!");
        }
        try {
            discord = new URL("https://discordapp.com/invite/4wayq37");
        } catch (MalformedURLException ignored) {
            getLogger().error("Unable to locate CmdCalendar Discord!");
        }

        CommandSpec AnswerTrivia = CommandSpec.builder()
                .executor(new AnswerTrivia())
                .description(Text.of("Answer a TriviaQuest trivia"))
                .permission("triviaquest.answer.command")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.remainingJoinedStrings(Text.of("playerAnswer"))))
                .build();
        CommandSpec DisablePack = CommandSpec.builder()
                .executor(new DisablePack())
                .description(Text.of("Disables a TriviaQuest pack"))
                .permission("triviaquest.packs.disable")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("packName"))))
                .build();
        CommandSpec EnablePack = CommandSpec.builder()
                .executor(new DisablePack())
                .description(Text.of("Enables a TriviaQuest pack"))
                .permission("triviaquest.packs.enable")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("packName"))))
                .build();
        CommandSpec PostTrivia = CommandSpec.builder()
                .executor(new PostTrivia())
                .description(Text.of("Posts TriviaQuest trivia"))
                .permission("triviaquest.trivia.post")
                .build();
        CommandSpec ReloadTrivia = CommandSpec.builder()
                .executor(new ReloadTrivia())
                .description(Text.of("Reloads TriviaQuest trivia"))
                .permission("triviaquest.trivia.reload")
                .build();
        CommandSpec StartTriva = CommandSpec.builder()
                .executor(new StartTrivia())
                .description(Text.of("Starts automatic TriviaQuest trivia"))
                .permission("triviaquest.run.start")
                .build();
        CommandSpec StopTrivia = CommandSpec.builder()
                .executor(new StopTrivia())
                .description(Text.of("Stops automatic TriviaQuest trivia"))
                .permission("triviaquest.run.stop")
                .build();
        CommandSpec TriviaQuest = CommandSpec.builder()
                .executor(new Base())
                .description(Text.of("Opens in-game documentation"))
                .permission("triviaquest.base")
                .child(AnswerTrivia, "AnswerTrivia", "Answer", "ans")
                .child(EnablePack, "EnablePack", "Enable", "ep")
                .child(DisablePack, "DisablePack", "Disable", "dp")
                .child(PostTrivia, "PostTrivia", "Post", "pt")
                .child(ReloadTrivia, "ReloadTrivia", "Reload", "rt")
                .child(StartTriva, "StartTrivia", "Start", "on")
                .child(StopTrivia, "StopTrivia", "Stop", "off")
                .build();
        Sponge.getCommandManager().register(this, TriviaQuest, Lists.newArrayList("TriviaQuest", "Trivia", "tq"));
    }

    @Listener
    public void onMessageSend(MessageChannelEvent.Chat event, @First Player player) {
        if (Config.isCheckMessages() && Trivia.isTriviaActive() && player.hasPermission("triviaquest.answer.chat")) {
            if (Trivia.checkAnswer(player, event.getRawMessage().toPlain())) {
                event.setMessageCancelled(true);
            }
        }
    }

    @Listener
    public void onReload(GameReloadEvent event) {
        Config.readConfig();
    }
}