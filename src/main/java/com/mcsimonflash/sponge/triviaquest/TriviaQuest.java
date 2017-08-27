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
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

@Plugin(id = "triviaquest", name = "TriviaQuest", version = "s6.0-v2.0.1", description = "In-Game Trivia Questions", authors = "Simon_Flash")
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
    @ConfigDir(sharedRoot = true)
    private Path directory;
    public Path getDirectory() {
        return directory;
    }

    @Listener
    public void onInitilization(GameInitializationEvent event) {
        plugin = this;
        getLogger().info("+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
        getLogger().info("|     TriviaQuest - Version 2.0.1     |");
        getLogger().info("|      Developed By: Simon_Flash      |");
        getLogger().info("+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
        Config.readConfig();
        try {
            wiki = new URL("https://github.com/SimonFlash/TriviaQuest/wiki");
            discord = new URL("https://discordapp.com/invite/4wayq37");
        } catch (MalformedURLException ignored) {
            getLogger().error("Unable to locate TriviaQuest Wiki / Support Discord!");
        }
        CommandSpec AnswerTrivia = CommandSpec.builder()
                .executor(new AnswerTrivia())
                .description(Text.of("Answer a TriviaQuest trivia"))
                .permission("triviaquest.answertrivia.base")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.remainingJoinedStrings(Text.of("answer"))))
                .build();
        CommandSpec PostTrivia = CommandSpec.builder()
                .executor(new PostTrivia())
                .description(Text.of("Posts TriviaQuest trivia"))
                .permission("triviaquest.posttrivia.base")
                .build();
        CommandSpec ToggleRunner = CommandSpec.builder()
                .executor(new ToggleRunner())
                .description(Text.of("Starts automatic TriviaQuest trivia"))
                .permission("triviaquest.togglerunner.base")
                .build();
        CommandSpec TriviaQuest = CommandSpec.builder()
                .executor(new Base())
                .description(Text.of("Opens in-game documentation"))
                .permission("triviaquest.base")
                .child(AnswerTrivia, "AnswerTrivia", "Answer", "ans")
                .child(PostTrivia, "PostTrivia", "Post", "pt")
                .child(ToggleRunner, "ToggleRunner", "Toggle", "tr")
                .build();
        Sponge.getCommandManager().register(plugin, TriviaQuest, Lists.newArrayList("TriviaQuest", "Trivia", "tq"));
    }

    @Listener
    public void onReload(GameReloadEvent event) {
        Config.readConfig();
    }

    @Listener(order= Order.EARLY)
    public void onMessageSend(MessageChannelEvent.Chat event, @Root Player player) {
        if (Trivia.trivia != null && player.hasPermission("triviaquest.answertrivia.chat")) {
            if (Trivia.processAnswer(player, event.getRawMessage().toPlain())) {
                event.setMessageCancelled(true);
            }
        }
    }
}