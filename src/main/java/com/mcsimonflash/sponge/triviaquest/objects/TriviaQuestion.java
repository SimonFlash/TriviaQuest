package com.mcsimonflash.sponge.triviaquest.objects;

public class TriviaQuestion {

    private String question;
    private String answer;

    public TriviaQuestion (String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion () { return question; }
    public String getAnswer () { return answer; }
}
