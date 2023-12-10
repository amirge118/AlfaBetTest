package org.example.models;
import com.sun.istack.NotNull;
import org.example.models.Location;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "my_schema", name = "events")
@DynamicUpdate
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Column(name = "name")
    private String name;
    @NotNull
    @Column(name = "date")
    private Date date;
    @NotNull
    @Column(name = "popularity")
    private int popularity;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_time", updatable = false)
    private Date creationTime;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    public Event() {}

    public Event(String name, Date date, int popularity, Location location) {
        this.name = name;
        this.date = date;
        this.popularity = popularity;
        this.location = location;
    }

    public Event(Integer id, String name, Date date, int popularity,Location location) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.popularity = popularity;
        this.location = location;
    }

    public Integer getId() {
        return id;
    }
    public Date getDate() {
        return date;
    }
    public int getPopularity() {
        return popularity;
    }
    public Date getCreationTime() {
        return creationTime;
    }
    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}
