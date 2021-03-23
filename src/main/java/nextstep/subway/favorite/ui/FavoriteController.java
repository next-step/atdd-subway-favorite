package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.IsExistFavoriteException;
import nextstep.subway.favorite.exception.NotFoundFavoriteException;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findAllFavoriteOfMine(
        @AuthenticationPrincipal LoginMember loginMember
    ) {
        return ResponseEntity.ok(
            favoriteService.findAllOfMember(loginMember.getId())
        );
    }

    @PostMapping
    public ResponseEntity<Void> addFavorite(
        @AuthenticationPrincipal LoginMember loginMember,
        @RequestBody FavoriteRequest favoriteRequest
    ) {
        FavoriteResponse favorite = favoriteService.addFavorite(loginMember.getId(), favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Void> removeFavorite(
        @AuthenticationPrincipal LoginMember loginMember,
        @PathVariable long favoriteId
    ) {
        favoriteService.removeFavorite(favoriteId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({
        IsExistFavoriteException.class,
        NotFoundFavoriteException.class
    })
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
