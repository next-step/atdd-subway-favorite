package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.NotFoundAuthenticationException;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.net.URI;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody FavoriteRequest favoriteRequest,
                                 @AuthenticationPrincipal LoginMember loginMember) throws AuthenticationException {
        FavoriteResponse favorite = favoriteService.save(loginMember.getId(), favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + loginMember.getId())).body(favorite);
    }

    @GetMapping
    public ResponseEntity getAll(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(favoriteService.getAll(loginMember.getId()));
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity remove(@PathVariable long favoriteId) {
        favoriteService.delete(favoriteId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NotFoundAuthenticationException.class)
    public ResponseEntity handleAuthenticationException(NotFoundAuthenticationException e) {
        return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
