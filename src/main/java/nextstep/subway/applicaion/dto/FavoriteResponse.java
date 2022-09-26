package nextstep.subway.applicaion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FavoriteResponse {
    Long id;
    StationDTO source;
    StationDTO target;

    @AllArgsConstructor
    public static class StationDTO {
        private Long id;
        private String name;
    }
}
