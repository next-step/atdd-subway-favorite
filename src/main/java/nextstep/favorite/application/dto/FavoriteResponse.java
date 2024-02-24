package nextstep.favorite.application.dto;

import nextstep.station.application.dto.StationDto;

/**
 * TODO: StationResponse를 포함하는 클래스로 만듭니다.
 */
public class FavoriteResponse {
    private Long id;
    private StationDto source;
    private StationDto target;

    public FavoriteResponse(Long id, StationDto source, StationDto target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public StationDto getSource() {
        return source;
    }

    public StationDto getTarget() {
        return target;
    }
}
