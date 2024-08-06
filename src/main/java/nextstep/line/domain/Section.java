package nextstep.line.domain;

import nextstep.exceptions.ErrorMessage;
import nextstep.line.exception.SectionDistanceNotValidException;
import nextstep.line.exception.SectionNotValidException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long upStationId;

    private Long downStationId;

    private Long distance;

    private Long position;

    protected Section() {

    }

    public Section(final Long upStationId, final Long downStationId, final Long distance) {
        validate(upStationId, downStationId, distance);
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    private void validate(final Long upStationId, final Long downStationId, final Long distance) {
        if(upStationId.equals(downStationId)) {
            throw new SectionNotValidException(ErrorMessage.DIFFERENT_STATIONS);
        }
        assertDistancePositive(distance);
    }

    public Long getId() {
        return id;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public void assertDistancePositive(Long distance) {
        if(distance < 1) {
            throw new SectionDistanceNotValidException(ErrorMessage.POSITIVE_DISTANCE);
        }
    }

    public void updateForNewSection(final Section nextStation) {
        Long newDistance = this.distance - nextStation.distance;
        assertDistancePositive(newDistance);
        this.upStationId = nextStation.downStationId;
        this.distance = newDistance;
    }

}
