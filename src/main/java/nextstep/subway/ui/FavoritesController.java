package nextstep.subway.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.FavoritesService;
import nextstep.subway.applicaion.dto.FavoritesRequest;
import nextstep.subway.applicaion.dto.FavoritesResponse;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoritesController {
    private final FavoritesService favoritesService;

    public FavoritesController(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@AuthenticationPrincipal LoginMember loginMember,
                                           @RequestBody FavoritesRequest favoritesRequest) {
        Long favoritesId = favoritesService.saveFavorites(favoritesRequest, loginMember);
        return ResponseEntity.created(URI.create("/favorites/" + favoritesId)).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoritesResponse>> showLines(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoritesResponse> responses = favoritesService.findByMemberId(loginMember);
        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> updateLine(@AuthenticationPrincipal LoginMember loginMember, Long id) {
        favoritesService.deleteFavorites(loginMember, id);
        return ResponseEntity.noContent().build();
    }
}
