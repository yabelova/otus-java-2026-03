package ru.otus.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("address")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Address {

    @Id
    private Integer id;

    private String street;

    public Address(String street) {
        this.street = street;
    }
}
