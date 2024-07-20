package nextstep.favorite.application.dto;

import lombok.Getter;

@Getter
public class FavoriteRequest {
  private final Long source;
  private final Long target;

  public FavoriteRequest() {
    this(null, null);
  }

  public FavoriteRequest(Long source, Long target) {
    this.source = source;
    this.target = target;
  }
}
