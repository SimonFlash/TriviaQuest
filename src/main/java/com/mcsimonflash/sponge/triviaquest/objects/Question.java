package com.mcsimonflash.sponge.triviaquest.objects;

import java.util.List;

public class Question implements ITrivia {

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
    public boolean checkAnswer(String str) {
        return Answers.stream().anyMatch(a -> a.equalsIgnoreCase(str));
    }
}
