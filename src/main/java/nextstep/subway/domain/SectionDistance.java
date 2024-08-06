package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.common.InvalidSectionException;

import javax.persistence.Embeddable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class SectionDistance {
    private int distance;

    public SectionDistance(int distance) {
        validatePositiveDistance(distance);
        this.distance = distance;
    }

    private void validatePositiveDistance(int distance) {
        if (distance <= 0) {
            throw new InvalidSectionException("구간 거리는 0보다 커야 합니다.");
        }
    }

    public SectionDistance minus(SectionDistance subtrahend) {
        int result = this.distance - subtrahend.distance;
        return new SectionDistance(result);
    }

    public SectionDistance plus(SectionDistance addend) {
        int result = this.distance + addend.distance;
        return new SectionDistance(result);  // 생성자에서 이미 양수 검증을 수행하므로 여기서는 별도 검증 불필요
    }

    public boolean isLessThanOrEqualTo(SectionDistance compareSectionDistance) {
        return this.distance <= compareSectionDistance.distance;
    }
}
