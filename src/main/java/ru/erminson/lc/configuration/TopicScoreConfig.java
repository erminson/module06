package ru.erminson.lc.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.erminson.lc.model.entity.TopicScore;

import java.time.Duration;

@Configuration
@ComponentScan("ru.erminson.lc")
@PropertySource("classpath:application.properties")
public class TopicScoreConfig {
    @Value("${topic_score.topic_title}")
    private String topicTitle;

    @Value("${topic_score.duration}")
    private int duration;

    @Bean("myTopic")
    public TopicScore createTopicScore() {
        return new TopicScore(topicTitle, Duration.ofDays(duration));
    }
}
