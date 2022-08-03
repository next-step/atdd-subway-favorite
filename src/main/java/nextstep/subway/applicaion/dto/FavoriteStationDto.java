package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.Station;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public class FavoriteStationDto {
    private final Long id;
    private final String name;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;


    public static FavoriteStationDto of(Station station) {
        return new FavoriteStationDto(station.getId(), station.getName(), station.getCreateDate(), station.getModifiedDate());
    }
}