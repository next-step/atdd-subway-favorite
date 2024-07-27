package nextstep.subway.domain.entity.station;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity(name = "stations")
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    protected Station() {}

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Station(String name) {
        this.name = name;
    }

    public boolean isSameStation(Station station) {
        return this.id.equals(station.id);
    }
}
