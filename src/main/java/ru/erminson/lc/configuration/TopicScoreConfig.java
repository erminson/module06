package ru.erminson.lc.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.erminson.lc.model.entity.TopicScore;

import java.time.Duration;

@Configuration
@ComponentScan("ru.erminson.lc")
public class TopicScoreConfig {
    @Bean("myTopic")
    public TopicScore createTopicScore() {
        return new TopicScore("TopicTitle", Duration.ofDays(2));
    }
}
