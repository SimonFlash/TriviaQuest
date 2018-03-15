package com.mcsimonflash.sponge.triviaquest.managers;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Util {

    public static Random random = new Random();

    public static Text toText(String msg) {
        return TextSerializers.FORMATTING_CODE.deserialize(msg);
    }

    public static Optional<String> getReward() {
        if (Config.chanceSum != 0) {
            double rand = random.nextDouble() * Config.chanceSum;
            for (Map.Entry<String, Integer> reward : Config.rewardCommands.entrySet()) {
                rand -= reward.getValue();
                if (rand <= 0) {
                    return Optional.of(reward.getKey());
                }
            }
        }
        return Optional.empty();
    }

    public static String getCompletion(String word) {
        List<Integer> positions = IntStream.range(0, word.length()).boxed().collect(Collectors.toList());
        Collections.shuffle(positions);
        int blanks = (int) Math.ceil((1 + Math.random()) * word.length() / 4.0);
        StringBuilder completion = new StringBuilder(word);
        for (int i = 0; i < blanks; i++) {
            completion.setCharAt(positions.get(i), '_');
        }
        return completion.toString();
    }

    public static String getScramble(String word) {
        char[] scramble = word.toCharArray();
        for (int i = scramble.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char t = scramble[j];
            scramble[j] = scramble[i];
            scramble[i] = t;
        }
        return new String(scramble);
    }

}