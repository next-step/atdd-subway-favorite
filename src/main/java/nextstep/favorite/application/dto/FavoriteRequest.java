package nextstep.favorite.application.dto;

import lombok.Getter;

@Getter
public class FavoriteRequest {
  private final Long source;
  private final Long target;

  private FavoriteRequest(Long source, Long target) {
    this.source = source;
    this.target = target;
  }

  public static FavoriteRequest of(Long source, Long target) {
    return new FavoriteRequest(source, target);
  }
}
