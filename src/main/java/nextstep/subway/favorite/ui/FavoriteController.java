package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("favorites")
public class FavoriteController {

    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity createFavorite(@RequestBody FavoriteRequest favoriteRequest, @AuthenticationPrincipal LoginMember loginMember) {

        FavoriteResponse response = favoriteService.createFavorite(favoriteRequest, loginMember.getId());
        return ResponseEntity.created(URI.create("/favorites/"+response.getId())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> inquiryFavorite(@AuthenticationPrincipal LoginMember loginMember) {

        List<FavoriteResponse> responses = favoriteService.findByMemberId(loginMember.getId());
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("{id}")
    public ResponseEntity removeFavorite(@PathVariable Long id, @AuthenticationPrincipal LoginMember loginMember) {

        favoriteService.removeFavorite(id, loginMember.getId());
        return ResponseEntity.noContent().build();
    }
}
