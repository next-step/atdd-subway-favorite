package nextstep.favorite.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.favorite.domain.Favorite;
import nextstep.subway.applicaion.dto.StationResponse;

@Getter
@AllArgsConstructor
public class FavoriteResponse {

  Long id;
  StationResponse source;
  StationResponse target;

  public static FavoriteResponse from(Favorite favorite, StationResponse source, StationResponse target) {
    return new FavoriteResponse(favorite.getId(), source, target);
  }
}
