package nextstep.favorite.ui;

import nextstep.common.exception.dto.ErrorResponse;
import nextstep.common.exception.utils.ExceptionHandlerUtils;
import nextstep.favorite.domain.exception.CantAddFavoriteException;
import nextstep.favorite.domain.exception.NotMyFavoriteException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FavoriteExceptionHandler {

    @ExceptionHandler(CantAddFavoriteException.class)
    public ResponseEntity<ErrorResponse> cantAddFavoriteExceptionHandler(CantAddFavoriteException e) {
        FavoriteErrorCode errorCode = FavoriteErrorCode.CANT_ADD_FAVORITE;
        return ExceptionHandlerUtils.buildErrorResponse(errorCode, e);
    }

    @ExceptionHandler(NotMyFavoriteException.class)
    public ResponseEntity<ErrorResponse> notMyFavoriteExceptionHandler(NotMyFavoriteException e) {
        FavoriteErrorCode errorCode = FavoriteErrorCode.NOT_MY_FAVORITE;
        return ExceptionHandlerUtils.buildErrorResponse(errorCode, e);
    }
}
