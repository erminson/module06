package ru.erminson.lc.model.dto.yaml;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.erminson.lc.model.dto.RecordBookDto;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class YamlRecordBookList {
    List<RecordBookDto> recordBooks;
}
