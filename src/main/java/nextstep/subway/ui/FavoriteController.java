package nextstep.subway.ui;

import nextstep.auth.authentication.AuthenticationPrincipal;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<Void> addFavorite(
            @AuthenticationPrincipal final Member member,
            @RequestBody final FavoriteRequest favoriteRequest) {
        final Long favoriteId = favoriteService.saveFavorite(member.getEmail(), favoriteRequest);

        return ResponseEntity.created(URI.create("/favorites/" + favoriteId)).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> showFavorites(@AuthenticationPrincipal final Member member) {
        final List<FavoriteResponse> favoriteResponses = favoriteService.findFavorites(member.getEmail());
        return ResponseEntity.ok(favoriteResponses);
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(
            @PathVariable final Long favoriteId,
            @AuthenticationPrincipal final Member member) {
        favoriteService.deleteFavorite(member.getEmail(), favoriteId);
        return ResponseEntity.noContent().build();
    }
}
