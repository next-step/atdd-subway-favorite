package nextstep.favorite.ui;

import nextstep.auth.domain.AppMember;
import nextstep.auth.domain.AuthenticationMember;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
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

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(@AuthenticationMember AppMember appMember, @RequestBody FavoriteRequest favoriteRequest) {
        Long favoriteId = favoriteService.saveFavorite(appMember.getId(), favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteId)).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> showFavorites(@AuthenticationMember AppMember appMember) {
        List<FavoriteResponse> favoriteResponses = favoriteService.findFavorites(appMember.getId());
        return ResponseEntity.ok().body(favoriteResponses);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(@AuthenticationMember AppMember appMember, @PathVariable Long id) {
        favoriteService.deleteFavorite(id, appMember.getId());
        return ResponseEntity.noContent().build();
    }
}
