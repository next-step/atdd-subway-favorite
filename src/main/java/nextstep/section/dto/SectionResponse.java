package nextstep.section.dto;

import nextstep.station.dto.StationResponse;

public class SectionResponse {

    private Long lineId;

    private Long sectionId;

    private StationResponse upStationResponse;

    private StationResponse downStationResponse;

    private Long distance;

    public SectionResponse() {
    }

    public SectionResponse(Long lineId, Long sectionId, StationResponse upStationResponse, StationResponse downStationResponse, Long distance) {
        this.lineId = lineId;
        this.sectionId = sectionId;
        this.upStationResponse = upStationResponse;
        this.downStationResponse = downStationResponse;
        this.distance = distance;
    }

    public static SectionResponse of(final Long lineId, final Long sectionId, final StationResponse upStationResponse, final StationResponse downStationResponse, final Long distance) {
        return new SectionResponse(lineId, sectionId, upStationResponse, downStationResponse, distance);
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public StationResponse getUpStationResponse() {
        return upStationResponse;
    }

    public StationResponse getDownStationResponse() {
        return downStationResponse;
    }

    public Long getDistance() {
        return distance;
    }
}

