package nextstep.line.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import nextstep.station.domain.Station;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id")
    private Station upStation;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id")
    private Station downStation;
    private int distance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    public Section() {}

    public Section(Station upStation, Station downStation, int distance, Line line) {
        this(null, upStation, downStation, distance, line);
    }

    public Section(Long id, Station upStation, Station downStation, int distance, Line line) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.line = line;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public boolean isDownStation(Station station) {
        return downStation == station;
    }

    public boolean isUpStation(Station station) {
        return upStation == station;
    }

    public void updateDownStation(Station downStation) {
        this.downStation = downStation;
    }

    public void updateUpStation(Station upStation) {
        this.upStation = upStation;
    }

    public void decreaseDistance(Section section) {
        this.distance = distance - section.getDistance();
    }

    public void increaseDistance(Section section) {
        this.distance = distance + section.getDistance();
    }

    public void remove() {
        this.line = null;
    }

    public int getDistance() {
        return distance;
    }

    public Long getId() {
        return id;
    }

    public Long getUpStationId() {
        return upStation.getId();
    }

    public Long getDownStationId() {
        return downStation.getId();
    }

    public String getUpStationName() {
        return upStation.getName();
    }

    public String getDownStationName() {
        return downStation.getName();
    }
}
