package nextstep.subway.favorite.dto;

public class FavoriteRequest {
  private long source;
  private long target;

  public FavoriteRequest(long source, long target) {
    this.source = source;
    this.target = target;
  }

  public long getSource() {
    return source;
  }

  public void setSource(long source) {
    this.source = source;
  }

  public long getTarget() {
    return target;
  }

  public void setTarget(long target) {
    this.target = target;
  }
}

