package ru.erminson.lc.model.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@EqualsAndHashCode
@Getter
@AllArgsConstructor
public class RecordBook {
    private final long id;
    private final long courseId;
    private final String courseTitle;
    private final LocalDate startDate;
    private List<TopicScore> topics;

    public void setTopics(List<TopicScore> topics) {
        this.topics = topics;
    }

    public void addTopic(TopicScore topicScore) {
        topics.add(topicScore);
    }

    public TopicScore getTopicScoreByTitle(String topicTitle) {
        return topics.stream()
                .filter(topic -> topic.getTopicTitle().equals(topicTitle))
                .findFirst()
                .orElseGet(() -> new TopicScore("", Duration.ZERO));
    }

    public boolean isExistsTopic(String topicTitle) {
        return topics.stream()
                .map(TopicScore::getTopicTitle)
                .anyMatch(title -> title.equals(topicTitle));
    }

    public LocalDate getEndDateTopic(String topicTitle) {
        int index = topics.stream()
                .map(TopicScore::getTopicTitle)
                .collect(Collectors.toList())
                .indexOf(topicTitle);

        if (index < 0) {
            // TODO: Throw Exception
            return null;
        }

        LocalDate endDate = startDate;
        List<TopicScore> subTopics = topics.subList(0, index + 1);
        for (final TopicScore topicScore : subTopics) {
            endDate = endDate.plusDays(topicScore.getDurationInDays().toDays());
        }

        return endDate;
    }

    public LocalDate getEndDate() {
        long durationInHours = topics.stream()
                .mapToLong(topicScore -> topicScore.getDurationInDays().toDays())
                .sum();
        return startDate.plusDays(durationInHours);
    }

    public double getAverageScore() {
        return topics.stream()
                .mapToDouble(TopicScore::getScore)
                .average()
                .orElse(0.0);
    }

    public double getAverageScoreInBestCase() {
        return topics.stream()
                .mapToDouble(topicScore -> topicScore.getScore() == 0 ? 100.0 : topicScore.getScore())
                .average()
                .orElse(0.0);
    }
}
