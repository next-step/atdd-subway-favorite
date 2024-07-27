package nextstep.subway.domain.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SectionDistance {
    public static final String INVALID_DISTANCE_MESSAGE = "거리는 0보다 커야 합니다.";

    @Column(name = "distance", nullable = false)
    private Integer value;

    protected SectionDistance() {
    }

    public SectionDistance(Integer value) {
        if (value <= 0) {
            throw new IllegalArgumentException(INVALID_DISTANCE_MESSAGE);
        }

        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        SectionDistance that = (SectionDistance)object;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
