package ru.erminson.lc.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.erminson.lc.model.entity.TopicScore;

import java.time.Duration;

@Configuration
public class TopicScoreConfig {
    @Value("${title}")
    private String topicTitle;

    @Value("${duration}")
    private int duration;

    @Bean("myTopic")
    public TopicScore createTopicScore() {
        return new TopicScore(topicTitle, Duration.ofDays(duration));
    }
}
