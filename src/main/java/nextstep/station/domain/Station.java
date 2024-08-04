package nextstep.station.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;

    public Station() {
    }

    private Station(String name) {
        this.name = name;
    }

    private Station(Station station) {
        this.id = station.getId();
        this.name = station.getName();
    }

    private Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Station from(String name) {
        return new Station(name);
    }

    public static Station from(Station station) {
        return new Station(station);
    }

    public static Station of(Long id, String name) {
        return new Station(id, name);
    }

    public boolean isSameId(Long stationId) {
        return id.equals(stationId);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Station station = (Station) o;
        return Objects.equals(id, station.id) && Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
