package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.UserDetails;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoritePathResponse;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.NoAuthorizedException;
import nextstep.subway.path.application.PathService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping()
public class FavoriteController {

    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService  favoriteService,PathService pathService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity saveFavorite(@AuthenticationPrincipal UserDetails loginMember, @RequestBody FavoriteRequest param) {
        final Favorite favorite = favoriteService.saveFavorite(loginMember, param);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<FavoritePathResponse> getFavorites(@AuthenticationPrincipal UserDetails loginMember) {
        return ResponseEntity.ok(FavoritePathResponse.of((favoriteService.getFavorites(loginMember))));
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity< FavoriteResponse > deleteFavorite(@AuthenticationPrincipal UserDetails loginMember, @PathVariable Long id) {
        favoriteService.deleteFavorite(loginMember, id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NoAuthorizedException.class)
    protected ResponseEntity handleMethodArgumentNotValidException(NoAuthorizedException e) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
