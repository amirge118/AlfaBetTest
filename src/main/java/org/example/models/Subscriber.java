package org.example.models;

import javax.persistence.*;

@Entity
@Table(schema = "my_schema", name = "subscribers")
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String mail;
    public Subscriber() {
    }
    public Subscriber(String name, String mail) {
        this.name = name;
        this.mail = mail;
    }
    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }


}
