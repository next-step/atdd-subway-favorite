package nextstep.subway.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteResponse;
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
    public ResponseEntity<Long> createFavorite(@AuthenticationPrincipal final LoginMember loginMember,
                                              @RequestBody final FavoriteSaveRequest request) {

        final Long id = favoriteService.createFavorite(loginMember.getId(), request.getSource(), request.getTarget());
        return ResponseEntity.created(URI.create("/favorites/" + id)).body(id);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findAllFavorite(@AuthenticationPrincipal final LoginMember loginMember) {
        final List<FavoriteResponse> favoriteResponses = favoriteService.findAllFavorite(loginMember.getId()).stream()
                .map(FavoriteResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(favoriteResponses);
    }
}
