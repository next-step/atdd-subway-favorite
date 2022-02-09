package nextstep.subway.ui;


import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.command.FavoriteCommandService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteCommandService favoriteCommandService;

    public FavoriteController(FavoriteCommandService favoriteCommandService) {
        this.favoriteCommandService = favoriteCommandService;
    }

    @PostMapping
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                               @RequestBody FavoriteRequest request) {
        favoriteCommandService.createFavorite(loginMember.getId(), request);
        return ResponseEntity.created(URI.create("/favorites"))
                .build();
    }

    @GetMapping
    public ResponseEntity<FavoriteResponse> findFavorite(@AuthenticationPrincipal LoginMember loginMember) {
        FavoriteResponse response = null;
        return ResponseEntity.ok(response);
    }

}
