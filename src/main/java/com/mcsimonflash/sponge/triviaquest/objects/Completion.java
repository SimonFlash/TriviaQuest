package com.mcsimonflash.sponge.triviaquest.objects;

import com.mcsimonflash.sponge.triviaquest.managers.Config;
import com.mcsimonflash.sponge.triviaquest.managers.Util;

import java.util.List;

public class Completion implements Trivia {

    public String Word;
    public List<String> Choices;

    public Completion(String word, List<String> choices) {
        Word = word;
        Choices = choices;
    }

    @Override
    public String getQuestion() {
        return Config.completionQuestion.replace("<word>", Choices.isEmpty() ? Util.getCompletion(Word) : Choices.get(Util.random.nextInt(Choices.size())));
    }

    @Override
    public String getAnswer() {
        return Config.completionAnswer.replace("<word>", Word);
    }

    @Override
    public boolean checkAnswer(String str) {
        return str.equalsIgnoreCase(Word);
    }
}
