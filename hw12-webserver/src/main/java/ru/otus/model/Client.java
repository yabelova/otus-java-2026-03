/*
 * Source: Module hw10-hibernate
 * Changes: Add smart save
 */
package ru.otus.model;

import jakarta.persistence.*;

import java.util.ArrayList;
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
    private List<Phone> phones = new ArrayList<>();

    public Client(String name, Address address, List<Phone> phones) {
        this.name = name;
        this.setAddress(address);
        this.setPhones(phones);
    }

    public void setAddress(Address address) {
        this.address = address;
        if (address != null) {
            address.setClient(this);
        }
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones != null ? phones : new ArrayList<>();
        this.phones.forEach(phone -> phone.setClient(this));
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
