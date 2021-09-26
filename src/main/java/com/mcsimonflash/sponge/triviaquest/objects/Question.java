package com.mcsimonflash.sponge.triviaquest.objects;

import com.mcsimonflash.sponge.triviaquest.managers.Config;

import java.util.List;

public class Question implements Trivia {

    public String Question;
    public List<String> Answers;

    public Question(String question, List<String> answers) {
        Question = question;
        Answers = answers;
    }

    @Override
    public String getQuestion() {
        return Question;
    }

    @Override
    public String getAnswer() {
        return Config.getAnswer.replace("<word>", String.join(", ", Answers));
    }

    @Override
    public boolean checkAnswer(String str) {
        return Answers.stream().anyMatch(a -> a.equalsIgnoreCase(str));
    }
}
