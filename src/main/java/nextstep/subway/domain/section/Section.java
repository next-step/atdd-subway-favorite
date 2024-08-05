package nextstep.subway.domain.section;

import nextstep.subway.domain.station.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "up_station_id")
    private Station upStation;
    @OneToOne
    @JoinColumn(name = "down_station_id")
    private Station downStation;
    @Column(nullable = false)
    private int distance;

    public Section() {
    }

    public Section(Station upwardStation, Station downwardStation, int distance) {
        this.upStation = upwardStation;
        this.downStation = downwardStation;
        this.distance = distance;
    }

    public void updateSection(Station upStation, Station downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Station getUpwardStation() {
        return upStation;
    }

    public Station getDownwardStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return distance == section.distance && Objects.equals(id, section.id) && Objects.equals(upStation, section.upStation) && Objects.equals(downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upStation, downStation, distance);
    }
}
