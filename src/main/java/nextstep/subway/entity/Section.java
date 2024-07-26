package nextstep.subway.entity;

import nextstep.subway.exception.IllegalSectionException;

import javax.persistence.*;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Line line;

    @ManyToOne(fetch = FetchType.LAZY)
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY)
    private Station downStation;

    private Long distance;

    protected Section() {
    }

    public Section(Line line, Station upStation, Station downStation, Long distance) {
        validateStations(upStation, downStation);
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public boolean containsStation(Station station) {
        return upStation.equals(station) || downStation.equals(station);
    }

    private static void validateStations(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new IllegalSectionException("구간의 상행역과 하행역이 같을수없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Long getDistance() {
        return distance;
    }

    public void setLine(Line line) {
        this.line = line;
    }

}
