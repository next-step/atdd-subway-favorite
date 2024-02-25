package nextstep.favorite.application.dto;

import nextstep.favorite.domain.Favorite;
import nextstep.station.application.dto.StationDto;

public class FavoriteDto {
    private Long id;
    private StationDto source;
    private StationDto target;

    public FavoriteDto(Long id, StationDto source, StationDto target) {
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

    public static FavoriteDto from(Favorite favorite) {
        StationDto source = StationDto.from(favorite.getSourceStation());
        StationDto target = StationDto.from(favorite.getTargetStation());
        return new FavoriteDto(favorite.getId(), source, target);
    }
}
