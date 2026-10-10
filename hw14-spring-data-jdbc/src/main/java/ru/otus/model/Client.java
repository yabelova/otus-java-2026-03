package ru.otus.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Table("client")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Client {

    @Id
    private Integer id;

    private String name;

    @MappedCollection(idColumn = "client_id")
    private Address address;

    @MappedCollection(idColumn = "client_id")
    private Set<Phone> phones = Set.of();

    public Client(String name, Address address, Set<Phone> phones) {
        this.name = name;
        this.address = address;
        this.phones = phones != null ? phones : Set.of();
    }
}
