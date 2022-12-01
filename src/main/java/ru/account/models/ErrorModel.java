package ru.account.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorModel {
    private String date;
    private Integer code;
    private String text;

    public ErrorModel(Integer code, String text) {
        this.date = Instant.now().toString();
        this.code = code;
        this.text = text;
    }
}
