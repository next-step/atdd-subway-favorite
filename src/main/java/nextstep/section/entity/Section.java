package nextstep.section.entity;

import nextstep.section.exception.SectionException;
import nextstep.station.entity.Station;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

import static nextstep.common.constant.ErrorCode.SECTION_DISTANCE_TOO_SHORT;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UP_STATION_ID")
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOWN_STATION_ID")
    private Station downStation;

    @NotNull
    private Long distance;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "PREVIOUS_SECTION_ID")
    private Section previousSection;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "NEXT_SECTION_ID")
    private Section nextSection;

    public Section() {
    }

    public Section(Long id, Station upStation, Station downStation, Long distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(final Long id, final Station upStation, final Station downStation, final Long distance) {
        if (distance < 1) {
            throw new SectionException(String.valueOf(SECTION_DISTANCE_TOO_SHORT));
        }

        return new Section(id, upStation, downStation, distance);
    }

    public static Section of(final Station upStation, final Station downStation, final Long distance) {
        if (distance < 1) {
            throw new SectionException(String.valueOf(SECTION_DISTANCE_TOO_SHORT));
        }

        return of(null, upStation, downStation, distance);
    }

    public Long getId() {
        return id;
    }

    public List<Station> getStations() {
        return List.of(upStation, downStation);
    }

    public Station getUpStation() {
        return this.upStation;
    }

    public Station getDownStation() {
        return this.downStation;
    }

    public Long getDistance() {
        return this.distance;
    }

    public Section getPreviousSection() {
        return previousSection;
    }

    public void setPreviousSection(Section previousSection) {
        this.previousSection = previousSection;
    }

    public void setUpStation(Station upStation) {
        this.upStation = upStation;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public Section getNextSection() {
        return nextSection;
    }

    public void setNextSection(Section nextSection) {
        this.nextSection = nextSection;
    }

    public void setDownStation(Station downStation) {
        this.downStation = downStation;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Section section = (Section) obj;
        return Objects.equals(id, section.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

