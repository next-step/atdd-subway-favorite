package nextstep.subway.domain.model;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "line_id", nullable = false)
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id", nullable = false)
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id", nullable = false)
    private Station downStation;

    @Embedded
    private SectionDistance distance;



    protected Section() {
    }

    public Section(Line line, Station upStation, Station downStation, Integer distance) {
        this(null, line, upStation, downStation, distance);
    }

    public Section(
        Long id,
        Line line,
        Station upStation,
        Station downStation,
        Integer distance
    ) {
        this.id = id;
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new SectionDistance(distance);
    }

    public static SectionBuilder builder() {
        return new SectionBuilder();
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

    public Long getUpStationId() {
        return upStation.getId();
    }

    public String getUpStationName() {
        return upStation.getName();
    }

    public Long getDownStationId() {
        return downStation.getId();
    }

    public String getDownStationName() {
        return downStation.getName();
    }

    public Station getDownStation() {
        return downStation;
    }

    public Integer getDistance() {
        return distance.getValue();
    }

    public void updateLine(Line line) {
        this.line = line;
    }

    public void updateUpStation(Station station, Integer distance) {
        this.upStation = station;
        this.distance = new SectionDistance(distance);
    }

    public void updateDownStation(Station station, Integer distance) {
        this.downStation = station;
        this.distance = new SectionDistance(distance);
    }

    public boolean hasSameUpStationWith(Station station) {
        return this.upStation.equals(station);
    }

    public boolean hasSameDownStationWith(Station station) {
        return this.downStation.equals(station);
    }

    public boolean hasDifferentBothStationsWith(Station station) {
        return !this.upStation.equals(station) && !this.downStation.equals(station);
    }

    public boolean areBothStationsSame() {
        if (this.upStation == null) {
            return downStation == null;
        }

        return this.upStation.equals(this.downStation);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Section section = (Section)object;
        return Objects.equals(line, section.line)
            && Objects.equals(upStation, section.upStation) &&
            Objects.equals(downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, upStation, downStation);
    }

    public static class SectionBuilder {
        private Long id;
        private Line line;
        private Station upStation;
        private Station downStation;
        private Integer distance;

        public SectionBuilder() {
        }

        public SectionBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public SectionBuilder line(Line line) {
            this.line = line;
            return this;
        }

        public SectionBuilder upStation(Station upStation) {
            this.upStation = upStation;
            return this;
        }

        public SectionBuilder downStation(Station downStation) {
            this.downStation = downStation;
            return this;
        }

        public SectionBuilder distance(Integer distance) {
            this.distance = distance;
            return this;
        }

        public Section build() {
            return new Section(this.id, this.line, this.upStation, this.downStation, this.distance);
        }
    }
}


