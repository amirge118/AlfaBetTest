package org.example.repository;

import org.example.models.Event;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {

    Optional<Event> findById(int id);
    @Query("SELECT e FROM Event e JOIN e.location l WHERE l.name = :locationName")
    List<Event> findByLocation(@Param("locationName") String locationName);
    @Query("SELECT e FROM Event e JOIN e.location l WHERE l.name = :locationName")
    List<Event> findByLocation(@Param("locationName") String locationName, Sort sort);
    int countByLocationId(int locationId);
}
