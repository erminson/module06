package ru.erminson.lc.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    private long id;
    private String title;
    private BigDecimal price;
    private List<Topic> topics;

    public Course(long id, String title, BigDecimal price) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.topics = new ArrayList<>();
    }

    public void addTopic(Topic topic) {
        topics.add(topic);
    }
}
