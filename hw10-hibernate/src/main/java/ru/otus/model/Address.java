package ru.otus.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"client"})
@Entity
public class Address {
    @Id
    @SequenceGenerator(name = "address_gen", sequenceName = "address_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_gen")
    private Long id;

    private String street;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Address(String street) {
        this.street = street;
    }
}
