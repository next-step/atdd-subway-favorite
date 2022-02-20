package nextstep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {
	@org.springframework.web.bind.annotation.ExceptionHandler(CantDeleteFavoriteException.class)
	public ResponseEntity<HttpStatus> checkCanDeleteFavorite() {
		return ResponseEntity.badRequest().build();
	}

}