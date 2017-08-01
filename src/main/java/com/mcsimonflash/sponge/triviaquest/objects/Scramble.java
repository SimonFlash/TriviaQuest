package com.mcsimonflash.sponge.triviaquest.objects;

import java.util.List;
import java.util.Random;

public class Scramble implements ITrivia {

    public String Word;
    public List<String> Choices;

    public Scramble(String word, List<String> choices) {
        Word = word;
        Choices = choices;
    }

    @Override
    public String getQuestion() {
        return "Unscramble the letters! " + Choices.get(new Random().nextInt(Choices.size()));
    }

    @Override
    public boolean checkAnswer(String str) {
        return str.equalsIgnoreCase(Word);
    }
}
