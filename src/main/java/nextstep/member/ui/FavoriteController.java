package nextstep.member.ui;

import nextstep.config.auth.Auth;
import nextstep.config.auth.context.Authentication;
import nextstep.member.application.FavoriteService;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
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

    @PostMapping
    public ResponseEntity<FavoriteResponse> createFavorite(@Auth Authentication authentication, @RequestBody FavoriteRequest favoriteRequest) {
        FavoriteResponse favoriteResponse = favoriteService.saveFavorite(authentication.getMemberResponse().getId(), favoriteRequest);
        return ResponseEntity.created(URI.create(String.format("/favorites/%s", favoriteResponse.getId()))).body(favoriteResponse);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@Auth Authentication authentication) {
        return ResponseEntity.ok().body(favoriteService.findFavorites(authentication.getMemberResponse().getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@Auth Authentication authentication, @PathVariable("id") Long id) {
        favoriteService.deleteFavorite(authentication.getMemberResponse().getId(), id);
        return ResponseEntity.noContent().build();
    }
}
