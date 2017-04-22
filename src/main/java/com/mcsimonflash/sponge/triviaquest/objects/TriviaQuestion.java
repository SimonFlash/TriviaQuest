package com.mcsimonflash.sponge.triviaquest.objects;

import com.google.common.collect.Lists;

import java.util.List;

public class TriviaQuestion {

    private String question;
    private List<String> answer = Lists.newArrayList();

    public TriviaQuestion(List<String> quesAnsList) {
        this.question = quesAnsList.get(0);
        for (int i = 1; i < quesAnsList.size(); i++) {
            answer.add(quesAnsList.get(i));
        }
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswer() {
        return answer;
    }
}
