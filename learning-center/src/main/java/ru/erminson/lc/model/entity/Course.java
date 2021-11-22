package ru.erminson.lc.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    private long id;
    private String title;
    private List<Topic> topics;

    public void addTopic(Topic topic) {
        topics.add(topic);
    }
}
