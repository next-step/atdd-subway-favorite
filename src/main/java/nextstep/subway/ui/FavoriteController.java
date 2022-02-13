package nextstep.subway.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteReadResponse;
import nextstep.subway.applicaion.dto.FavoriteSaveRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/favorites")
@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<URI> createFavorite(@AuthenticationPrincipal final LoginMember loginMember,
                                              @RequestBody final FavoriteSaveRequest request) {

        final Long id = favoriteService.createFavorite(loginMember.getId(), request.getSource(), request.getTarget());
        return ResponseEntity.created(URI.create("/favorites/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteReadResponse>> findAllFavorite(@AuthenticationPrincipal final LoginMember loginMember) {
        final List<FavoriteReadResponse> favoriteResponses = favoriteService.findAllFavorite(loginMember.getId()).stream()
                .map(FavoriteReadResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(favoriteResponses);
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal final LoginMember loginMember, @PathVariable Long favoriteId) {
        favoriteService.delete(loginMember.getId(), favoriteId);
        return ResponseEntity.noContent().build();
    }
}
