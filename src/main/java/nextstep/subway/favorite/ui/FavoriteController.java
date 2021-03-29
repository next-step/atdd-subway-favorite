package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.exception.UnauthorizedException;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.NoContentsException;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity createFavorite(@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest favoriteRequest) {
        if(favoriteRequest.isValidRequest()) {
            FavoriteResponse favorite = favoriteService.createFavorite(loginMember.getId(), favoriteRequest);
            return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).body(favorite);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(favoriteService.getFavorites(loginMember.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        favoriteService.deleteFavorite(loginMember.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({UnauthorizedException.class, NoContentsException.class})
    public ResponseEntity handleUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
