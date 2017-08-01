package com.mcsimonflash.sponge.triviaquest.managers;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Map;

public class Util {

    public static Text toText(String msg) {
        return TextSerializers.FORMATTING_CODE.deserialize(msg);
    }

    public static String getReward() {
        if (Config.chanceSum != 0) {
            double randSelec = Config.chanceSum * Math.random();
            for (Map.Entry<String, Integer> reward : Config.rewardCommands.entrySet()) {
                randSelec -= reward.getValue();
                if (randSelec <= 0) {
                    return reward.getKey();
                }
            }
            throw new RuntimeException("Reached end of reward list without selection!");
        }
        return "";
    }
}
