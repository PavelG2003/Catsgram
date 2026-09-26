package ru.yandex.practicum.catsgram.dto.post;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class FindAllPostRequest {

    @Min(value = 0, message = "Начало выборки должно быть не меньше нуля")
    private int from = 0;

    @Min(value = 1, message = "Размер выборки должен быть больше нуля")
    private int size = 10;

    @Pattern(
            regexp = "ascending|asc|descending|desc",
            message = "Допустимые варианты: ascending, asc, descending, desc"
    )
    private String sort = "desc";
}
