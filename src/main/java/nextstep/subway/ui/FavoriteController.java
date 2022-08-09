package nextstep.subway.ui;

import nextstep.auth.authentication.UserDetails;
import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/favorites")
    public ResponseEntity getFavorites(@AuthenticationPrincipal UserDetails userDetails) {
        List<FavoriteResponse> favorites = favoriteService.getFavorites(userDetails.getEmail());
        return ResponseEntity.ok()
                .body(favorites);
    }

    @PostMapping("/favorites")
    public ResponseEntity saveFavorites(@AuthenticationPrincipal UserDetails userDetails, @RequestBody FavoriteRequest request) {
        FavoriteResponse response = favoriteService.saveFavorites(userDetails.getEmail(), request);
        return ResponseEntity.created(URI.create("/favorites/" + response.getId()))
                .build();
    }

    @DeleteMapping("/favorites/{favoriteId}")
    public ResponseEntity deleteFavorites(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long favoriteId) {
        favoriteService.deleteFavorite(userDetails.getEmail(), favoriteId);
        return ResponseEntity.noContent()
                .build();
    }
}
