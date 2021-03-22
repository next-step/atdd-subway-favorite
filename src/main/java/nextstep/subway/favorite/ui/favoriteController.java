package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.exceptions.UnauthorizedException;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class favoriteController {
    private final FavoriteService favoriteService;

    public favoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public ResponseEntity readFavorites(@AuthenticationPrincipal LoginMember member) {
        List<FavoriteResponse> results = favoriteService.findFavoritesAllByMemberId(member.getId());
        return ResponseEntity.ok().body(results);
    }

    @PostMapping
    public ResponseEntity createFavorites(@AuthenticationPrincipal LoginMember member,
                                          @RequestBody FavoriteRequest request) {
        FavoriteResponse favoriteResponse= favoriteService.saveFavorites(member.getId(), request);
        return ResponseEntity.created(URI.create("/favorites/"+favoriteResponse.getId())).build();
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity deleteFavorites(@AuthenticationPrincipal LoginMember member,
                                          @PathVariable Long favoriteId) {
        favoriteService.deleteFavorite(member.getId(), favoriteId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity handleUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
