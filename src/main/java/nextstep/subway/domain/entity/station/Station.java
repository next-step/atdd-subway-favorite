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

    public Station(String name) {
        this.name = name;
    }
}
