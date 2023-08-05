package nextstep.subway.applicaion.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import nextstep.subway.domain.Station;

@Entity
@Table(name = "station")
public class StationData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public StationData() {
    }

    public StationData(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationData of(Station station) {
        return new StationData(station.getId(), station.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
