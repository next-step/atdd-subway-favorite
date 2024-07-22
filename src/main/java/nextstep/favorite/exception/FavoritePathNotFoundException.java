package nextstep.favorite.exception;

import nextstep.support.error.ApiException;
import nextstep.support.error.ErrorCode;

public class FavoritePathNotFoundException extends ApiException {
  public FavoritePathNotFoundException() {
    super(ErrorCode.FAVORITE_PATH_NOT_FOUND);
  }
}
