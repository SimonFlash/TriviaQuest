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
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.net.MalformedURLException;
import java.net.URL;

@Plugin(id = "triviaquest", name = "TriviaQuest", version = "2.1.0", description = "In-Game Trivia Questions", authors = "Simon_Flash")
public class TriviaQuest {

    private static TriviaQuest instance;
    private static PluginContainer container;
    private static Logger logger;
    private static URL wiki;
    private static URL discord;

    @Inject
    public TriviaQuest(PluginContainer container) {
        instance = this;
        TriviaQuest.container = container;
        logger = container.getLogger();
    }

    @Listener
    public void onInitilization(GameInitializationEvent event) {
        logger.info("+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
        logger.info("|     TriviaQuest - Version 2.1.0     |");
        logger.info("|      Developed By: Simon_Flash      |");
        logger.info("+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
        Config.readConfig();
        try {
            wiki = new URL("https://github.com/SimonFlash/TriviaQuest/wiki");
            discord = new URL("https://discordapp.com/invite/4wayq37");
        } catch (MalformedURLException ignored) {
            logger.error("Unable to locate TriviaQuest Wiki / Support Discord!");
        }
        CommandSpec AnswerTrivia = CommandSpec.builder()
                .executor(new AnswerTrivia())
                .description(Text.of("Answer a TriviaQuest trivia"))
                .permission("triviaquest.answertrivia.base")
                .arguments(GenericArguments.remainingJoinedStrings(Text.of("answer")))
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
                .child(AnswerTrivia, "answertrivia", "answer", "ans")
                .child(PostTrivia, "posttrivia", "post", "pt")
                .child(ToggleRunner, "togglerunner", "toggle", "tr")
                .build();
        Sponge.getCommandManager().register(instance, TriviaQuest, "triviaquest", "trivia", "tq");
    }

    @Listener
    public void onReload(GameReloadEvent event) {
        Config.readConfig();
    }

    @Listener(order = Order.EARLY)
    public void onMessageSend(MessageChannelEvent.Chat event, @Root Player player) {
        if (Trivia.trivia != null && player.hasPermission("triviaquest.answertrivia.chat")) {
            if (Trivia.processAnswer(player, event.getRawMessage().toPlain())) {
                event.setMessageCancelled(true);
            }
        }
    }

    public static TriviaQuest getInstance() {
        return instance;
    }
    public static PluginContainer getContainer() {
        return container;
    }
    public static Logger getLogger() {
        return logger;
    }
    public static URL getWiki() {
        return wiki;
    }
    public static URL getDiscord() {
        return discord;
    }

}