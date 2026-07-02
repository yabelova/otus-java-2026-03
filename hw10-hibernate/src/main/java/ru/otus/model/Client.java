package ru.otus.model;

import jakarta.persistence.*;

import java.util.List;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@NamedEntityGraph(name = "client-with-all", attributeNodes = {
        @NamedAttributeNode("address"),
        @NamedAttributeNode("phones")
})
public class Client {

    @Id
    @SequenceGenerator(name = "client_gen", sequenceName = "client_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    private Long id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "client")
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client")
    private List<Phone> phones;

    public Client(String name, Address address, List<Phone> phones) {
        this.name = name;
        this.address = address;
        this.phones = phones;
    }
}
