package subway.member.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.auth.principal.AuthenticationPrincipal;
import subway.auth.principal.UserPrincipal;
import subway.member.application.FavoriteService;
import subway.member.application.dto.FavoriteCreateRequest;
import subway.member.application.dto.FavoriteCreateResponse;
import subway.member.application.dto.FavoriteRetrieveResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal UserPrincipal principal,
                                               @RequestBody FavoriteCreateRequest request) {
        FavoriteCreateResponse favoriteResponse = favoriteService.createFavorite(principal, request);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteRetrieveResponse>> retrieveFavorites(@AuthenticationPrincipal UserPrincipal principal) {
        List<FavoriteRetrieveResponse> favorites = favoriteService.retrieveFavorite(principal);
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal UserPrincipal principal,
                                               @PathVariable Long id) {
        favoriteService.deleteFavorite(principal, id);
        return ResponseEntity.noContent().build();
    }
}
