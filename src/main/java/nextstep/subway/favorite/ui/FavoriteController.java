package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody FavoriteRequest favoriteRequest,
                                 @AuthenticationPrincipal LoginMember loginMember) {

        FavoriteResponse favorite = favoriteService.save(loginMember.getId(), favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + loginMember.getId())).body(favorite);
    }

    @GetMapping
    public ResponseEntity getAll(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(favoriteService.getAll(loginMember.getId()));
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity remove(@AuthenticationPrincipal LoginMember loginMember,
                                 @PathVariable long favoriteId) {
        favoriteService.delete(loginMember.getId(), favoriteId);
        return ResponseEntity.noContent().build();
    }
}
