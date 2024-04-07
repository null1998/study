package org.example.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.component.CustomLocalDateDeserializer;

import java.time.LocalDate;

/**
 * @author huang
 */
public class JsonDeserializeDTO {
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate startDate;

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}
