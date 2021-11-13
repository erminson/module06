package ru.erminson.lc.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.erminson.lc.annotation.InjectRandomMark;

import java.time.Duration;

@Data
@AllArgsConstructor
public class TopicScore {
    private final String topicTitle;
    private int score;
    private final Duration durationInDays;

    public TopicScore(String topicTitle, Duration durationInDays) {
        this.topicTitle = topicTitle;
        this.score = 0;
        this.durationInDays = durationInDays;
    }

    @InjectRandomMark
    public void setScore(int score) {
        this.score = score;
    }
}
