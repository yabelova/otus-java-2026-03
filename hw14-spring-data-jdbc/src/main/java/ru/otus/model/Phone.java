package ru.otus.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("phone")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Phone {

    @Id
    private Integer id;

    private String number;

    public Phone(String number) {
        this.number = number;
    }
}
