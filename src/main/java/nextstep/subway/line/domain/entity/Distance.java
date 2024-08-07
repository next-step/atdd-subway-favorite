package nextstep.subway.line.domain.entity;

import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.common.exception.SubwayExceptionType;

@Getter
@Embeddable
@NoArgsConstructor
public class Distance {

    private Long distance;

    public Distance(Long distance) {
        if (distance <= 0) {
            throw new SubwayException(SubwayExceptionType.INVALID_DISTANCE, distance.toString());
        }

        this.distance = distance;
    }

    public void updateDistance(Long distance) {
        if (distance <= 0) {
            throw new SubwayException(SubwayExceptionType.INVALID_DISTANCE, distance.toString());
        }
        this.distance = distance;
    }
}
