/*
 * Source: Module hw10-hibernate
 */
package ru.otus.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"client"})
@Entity
public class Phone {

    @Id
    @SequenceGenerator(name = "phone_gen", sequenceName = "phone_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phone_gen")
    private Long id;

    private String number;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Phone(String number) {
        this.number = number;
    }
}
