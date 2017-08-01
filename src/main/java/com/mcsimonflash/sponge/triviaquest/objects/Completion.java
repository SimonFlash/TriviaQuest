package com.mcsimonflash.sponge.triviaquest.objects;

import java.util.List;
import java.util.Random;

public class Completion implements ITrivia {

    public String Word;
    public List<String> Choices;

    public Completion(String word, List<String> choices) {
        Word = word;
        Choices = choices;
    }

    @Override
    public String getQuestion() {
        return "Fill in the blanks! " + Choices.get(new Random().nextInt(Choices.size()));
    }

    @Override
    public boolean checkAnswer(String str) {
        return str.equalsIgnoreCase(Word);
    }
}
