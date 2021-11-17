package ru.erminson.lc.model.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopicScoreReport {
    private String topicTitle;
    private String startDate;
    private String endDate;
    private int score;
}
