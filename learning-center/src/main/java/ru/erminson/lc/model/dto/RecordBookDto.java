package ru.erminson.lc.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordBookDto {
    private String studentName;
    private String courseTitle;
    private String startDate;
    private List<TopicScoreDto> topics;
}
