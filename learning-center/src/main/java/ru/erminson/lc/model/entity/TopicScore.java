package ru.erminson.lc.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.erminson.lc.annotation.InjectRandomMark;

import java.time.Duration;

@Data
@AllArgsConstructor
public class TopicScore {
    private long id;
    private long topicId;
    private final String topicTitle;
    private int score;
    private final Duration durationInDays;

    public TopicScore(String topicTitle, Duration durationInDays) {
        this.topicTitle = topicTitle;
        this.score = 0;
        this.durationInDays = durationInDays;
    }

    public TopicScore(long topicId, String topicTitle, Duration durationInDays) {
        this.topicId = topicId;
        this.topicTitle = topicTitle;
        this.score = 0;
        this.durationInDays = durationInDays;
    }

    public TopicScore(String topicTitle, int score, Duration durationInDays) {
        this.topicTitle = topicTitle;
        this.score = score;
        this.durationInDays = durationInDays;
    }

    @InjectRandomMark
    public void setScore(int score) {
        this.score = score;
    }
}
