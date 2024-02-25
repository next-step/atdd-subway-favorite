package nextstep.favorite.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class FavoriteCreateResponse {

  Long id;

  // station ID
  Long source;

  // station ID
  Long target;
}
