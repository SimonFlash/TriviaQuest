package com.mcsimonflash.sponge.triviaquest.objects;

public interface ITrivia {

    String getQuestion();

    String getAnswer();

    boolean checkAnswer(String str);
}
