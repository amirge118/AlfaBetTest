package org.example.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(schema = "my_schema", name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", unique = true)
    private String name;

    public Location(){}
    public Location(String name) {
        this.name = name;
    }

    public Location(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(id, location.id) && name.equals(location.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
