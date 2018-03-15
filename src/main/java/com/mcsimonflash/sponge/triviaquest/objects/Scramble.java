package com.mcsimonflash.sponge.triviaquest.objects;

import com.mcsimonflash.sponge.triviaquest.managers.Config;
import com.mcsimonflash.sponge.triviaquest.managers.Util;

import java.util.List;

public class Scramble implements Trivia {

    public String Word;
    public List<String> Choices;

    public Scramble(String word, List<String> choices) {
        Word = word;
        Choices = choices;
    }

    @Override
    public String getQuestion() {
        return Config.scrambleQuestion.replace("<word>", Choices.isEmpty() ? Util.getScramble(Word) : Choices.get(Util.random.nextInt(Choices.size())));
    }

    @Override
    public String getAnswer() {
        return Config.scrambleAnswer.replace("<word>", Word);
    }

    @Override
    public boolean checkAnswer(String str) {
        return str.equalsIgnoreCase(Word);
    }
}
