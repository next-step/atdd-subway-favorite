package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.infrastructure.UserDetails;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity create(
            @AuthenticationPrincipal UserDetails userDetails,
            Long source, Long target
    ) throws URISyntaxException {
        FavoriteResponse response = favoriteService.create(source, target, userDetails.getId());
        return ResponseEntity.created(new URI(String.format("/favorites/%d", response.getId()))).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity findAll(@AuthenticationPrincipal UserDetails userDetails){
        List<FavoriteResponse> favorites = favoriteService.findAllByMemberId(userDetails.getId());
        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("/favorites/{favoriteId}")
    public ResponseEntity delete(@PathVariable Long favoriteId) {
        favoriteService.delete(favoriteId);
        return ResponseEntity.noContent().build();
    }
}
