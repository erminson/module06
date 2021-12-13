package ru.erminson.lc.model.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class StudentRequest {
    @NotEmpty
    private String name;
}
