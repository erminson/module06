package ru.erminson.lc.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.erminson.lc.annotation.InjectRandomMark;

import java.time.Duration;

@Data
@AllArgsConstructor
public class TopicScore {
    private static final double TEACHING_HOURS_PER_DAY = 8;

    private long id;
    private long topicId;
    private final String topicTitle;
    private int score;
    private final Duration durationInDays;

    public static TopicScore create(long topicScoreId, Topic topic) {
        return create(topicScoreId, 0, topic);
    }

    public static TopicScore create(long topicScoreId, int score, Topic topic) {
        Duration durationInDays = Duration.ofDays((long) Math.ceil(topic.getDurationInHours() / TEACHING_HOURS_PER_DAY));
        return new TopicScore(topicScoreId, topic.getId(), topic.getTitle(), score, durationInDays);
    }

    public TopicScore(String topicTitle, int score, Duration durationInDays) {
        this.topicTitle = topicTitle;
        this.score = score;
        this.durationInDays = durationInDays;
    }

    public TopicScore(String topicTitle, Duration durationInDays) {
        this(topicTitle, 0, durationInDays);
    }

    public TopicScore(long topicId, String topicTitle, Duration durationInDays) {
        this(topicTitle, durationInDays);
        this.topicId = topicId;
    }

    public TopicScore(long id, long topicId, String topicTitle, Duration durationInDays) {
        this(topicId, topicTitle, durationInDays);
        this.id = id;
    }

    @InjectRandomMark
    public void setScore(int score) {
        this.score = score;
    }
}
