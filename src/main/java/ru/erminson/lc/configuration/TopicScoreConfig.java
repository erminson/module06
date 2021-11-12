package ru.erminson.lc.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import ru.erminson.lc.model.entity.TopicScore;

import java.time.Duration;

@ConfigurationProperties(prefix = "topic")
public class TopicScoreConfig {
    private String title;
    private int duration;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Bean
    public TopicScore createTopicScore() {
        return new TopicScore(title, Duration.ofDays(duration));
    }
}
