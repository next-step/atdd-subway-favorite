package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.subway.dto.FavoriteRequest;
import nextstep.subway.dto.FavoriteResponse;
import nextstep.subway.service.FavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/favorites")
    ResponseEntity createFavorite(@RequestBody FavoriteRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long favoriteId = favoriteService.createFavorite(request, userPrincipal.getUsername());
        return ResponseEntity.created(URI.create("/favorites/" + favoriteId)).build();
    }

    @GetMapping("/favorites")
    ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<FavoriteResponse> responseList = favoriteService.getFavorites(userPrincipal.getUsername());
        return ResponseEntity.ok()
                .body(responseList);
    }

    @DeleteMapping("/favorites/{id}")
    ResponseEntity deleteFavorite(@PathVariable long id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        favoriteService.deleteFavorite(id, userPrincipal.getUsername());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
