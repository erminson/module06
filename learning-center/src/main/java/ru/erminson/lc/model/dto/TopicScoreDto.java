package ru.erminson.lc.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicScoreDto {
    private String topicTitle;
    private int score;
    private int durationInDays;
}
