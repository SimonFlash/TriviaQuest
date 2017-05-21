package com.mcsimonflash.sponge.triviaquest.objects;

import com.google.common.collect.Lists;

import java.util.List;

public class TriviaQuestion {

    private boolean giveReward;
    private String question;
    private List<String> answer = Lists.newArrayList();

    public TriviaQuestion(String question, List<String> ansList, boolean giveReward) {
        this.question = question;
        answer.addAll(ansList);
        this.giveReward = giveReward;
    }

    public boolean isGiveReward() {
        return giveReward;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswer() {
        return answer;
    }
}
