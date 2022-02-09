package nextstep.subway.ui;


import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.command.FavoriteCommandService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.query.FavoriteQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteCommandService favoriteCommandService;
    private final FavoriteQueryService favoriteQueryService;

    public FavoriteController(FavoriteCommandService favoriteCommandService,
                              FavoriteQueryService favoriteQueryService) {
        this.favoriteCommandService = favoriteCommandService;
        this.favoriteQueryService = favoriteQueryService;
    }

    @PostMapping
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                               @RequestBody FavoriteRequest request) {
        favoriteCommandService.createFavorite(loginMember.getId(), request);
        return ResponseEntity.created(URI.create("/favorites"))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findFavorite(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> response = favoriteQueryService.findFavorite(loginMember.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                               @PathVariable Long favoriteId) {
        return ResponseEntity.ok().build();
    }

}
