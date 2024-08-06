package nextstep.station.entity;

import javax.persistence.*;

@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    protected Station() {
    }

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Station of(final String name) {
        return new Station(null, name);
    }

    public static Station of(final Long id, final String name) {
        return new Station(id, name);
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

