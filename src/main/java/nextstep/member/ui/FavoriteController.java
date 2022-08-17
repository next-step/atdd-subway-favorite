package nextstep.member.ui;

import nextstep.auth.secured.Secured;
import nextstep.member.application.FavoriteService;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
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
    public ResponseEntity<Void> createFavorite(@RequestBody FavoriteRequest favoriteRequest) {
        Long favoriteId = favoriteService.saveFavorite(favoriteRequest);
        return ResponseEntity.created(URI.create("/" + favoriteId)).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> showFavorites() {
        List<FavoriteResponse> responses = favoriteService.findFavoriteResponses();
        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id) {
        favoriteService.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }
}
