package nextstep.line.application.dto.section;

import nextstep.line.domain.Section;
import nextstep.station.application.dto.StationDto;

public class SectionDto {
    private Long id;
    private StationDto upStation;
    private StationDto downStation;
    private int distance;

    public SectionDto(Long id, StationDto upStation, StationDto downStation, int distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public StationDto getUpStation() {
        return upStation;
    }

    public StationDto getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }

    public static SectionDto from(Section section) {
        StationDto upStation = StationDto.from(section.getUpStation());
        StationDto downStation = StationDto.from(section.getDownStation());
        return new SectionDto(section.getId(), upStation, downStation, section.getDistance());
    }
}
