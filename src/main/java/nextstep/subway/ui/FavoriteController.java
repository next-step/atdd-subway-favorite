package nextstep.subway.ui;

import nextstep.auth.authentication.UserDetails;
import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> createFavorite(@RequestBody FavoriteRequest favoriteRequest, @AuthenticationPrincipal UserDetails userDetails) {
        FavoriteResponse favoriteResponse = favoriteService.saveFavorite(favoriteRequest, userDetails.getId());
        return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).body(favoriteResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FavoriteResponse> deleteFavorite(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
//        favoriteService.deleteFavorite(id, userDetails.getId());

        return ResponseEntity.noContent().build();
    }
}
