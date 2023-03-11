package nextstep.favorites.ui;

import java.net.URI;
import java.util.List;
import nextstep.common.LoginMember;
import nextstep.favorites.application.FavoriteService;
import nextstep.favorites.application.dto.FavoriteRequest;
import nextstep.favorites.application.dto.FavoriteResponse;
import nextstep.member.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<String> addFavorites(@LoginMember Long memberId, @RequestBody FavoriteRequest favoriteRequest) {
        Long favoriteId = favoriteService.addFavorite(memberId, favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteId)).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@LoginMember Long memberId) {
        return ResponseEntity.ok(favoriteService.getFavorites(memberId));
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Void> removeFavorite(@LoginMember Long memberId, @PathVariable Long favoriteId) {
        favoriteService.remove(memberId, favoriteId);
        return ResponseEntity.noContent().build();
    }
}
