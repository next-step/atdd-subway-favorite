package nextstep.subway.domain.entity.line;

import lombok.Getter;
import nextstep.subway.domain.exception.SubwayDomainException;
import nextstep.subway.domain.exception.SubwayDomainExceptionType;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity(name = "line_sections")
public class LineSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id", nullable = false)
    private Line line;

    @Column(nullable = false)
    private Long upStationId;

    @Column(nullable = false)
    private Long downStationId;

    @Column(nullable = false)
    private Long distance;

    @Column(nullable = false)
    private Long position;

    protected LineSection() {}

    public LineSection(Line line, Long upStationId, Long downStationId, Long distance) {
        this.line = line;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.position = 0L;
    }

    public void changePosition(Long position) {
        this.position = position;
    }

    public List<LineSection> split(Long middleStationId, Long firstDistance) {
        long secondDistance = distance - firstDistance;
        if (secondDistance < 0) {
            throw new SubwayDomainException(SubwayDomainExceptionType.INVALID_SECTION_DISTANCE);
        }

        LineSection first = new LineSection(line, upStationId, middleStationId, firstDistance);
        LineSection second = new LineSection(line, middleStationId, downStationId, secondDistance);

        return List.of(first, second);
    }

    public boolean isPrevSectionThan(LineSection section) {
        return downStationId.equals(section.getUpStationId());
    }

    public boolean isNextSectionThan(LineSection section) {
        return upStationId.equals(section.getDownStationId());
    }

    public boolean isSameUpStation(LineSection section) {
        return upStationId.equals(section.upStationId);
    }
}
