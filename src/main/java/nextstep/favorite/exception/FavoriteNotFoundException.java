package nextstep.favorite.exception;

import nextstep.support.error.ApiException;
import nextstep.support.error.ErrorCode;

public class FavoriteNotFoundException extends ApiException {
  public FavoriteNotFoundException(Long id) {
    super(ErrorCode.NOT_FOUND, "즐겨찾기 #" + id + "이 존재하지 않습니다.");
  }
}
