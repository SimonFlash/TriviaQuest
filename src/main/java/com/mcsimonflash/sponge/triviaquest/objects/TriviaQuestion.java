package com.mcsimonflash.sponge.triviaquest.objects;

import com.google.common.collect.Lists;

import java.util.List;

public class TriviaQuestion {

    private String question;
    private List<String> answer = Lists.newArrayList();

    public TriviaQuestion (List<String> quesAnsList) {
        for (int i = 0; i < quesAnsList.size(); i++) {
            if (i == 0) {
                this.question = quesAnsList.get(i);
            } else {
                answer.add(quesAnsList.get(i));
            }
        }
    }

    public String getQuestion () { return question; }
    public List<String> getAnswer () { return answer; }
}
