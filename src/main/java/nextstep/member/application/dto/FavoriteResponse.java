package nextstep.member.application.dto;

import nextstep.member.domain.Favorite;

public class FavoriteResponse {
  private Long id;
  private Long sourceId;
  private Long targetId;

  private FavoriteResponse() {}

  private FavoriteResponse(Long id, Long sourceId, Long targetId) {
    this.id = id;
    this.sourceId = sourceId;
    this.targetId = targetId;
  }

  public static FavoriteResponse of(Favorite favorite) {
    return new FavoriteResponse(favorite.getId(), favorite.getSourceId(), favorite.getTargetId());
  }

  public Long getId() {
    return id;
  }

  public Long getSourceId() {
    return sourceId;
  }

  public Long getTargetId() {
    return targetId;
  }
}
