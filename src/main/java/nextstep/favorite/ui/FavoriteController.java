package nextstep.favorite.ui;

import java.net.URI;
import java.util.List;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.application.dto.FavoriteSimpleResponse;
import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.ui.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final MemberService memberService;

    public FavoriteController(FavoriteService favoriteService, MemberService memberService) {
        this.favoriteService = favoriteService;
        this.memberService = memberService;
    }

    @PostMapping("/favorites")
    public ResponseEntity createFavorite(@AuthenticationPrincipal LoginMember loginMember,
        @RequestBody FavoriteRequest request) {
        FavoriteSimpleResponse favoriteResponse = favoriteService.createFavorite(loginMember,
            request);
        return ResponseEntity
            .created(URI.create("/favorites/" + favoriteResponse.getId()))
            .build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(
        @AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> favorites = favoriteService.findFavorites(loginMember);
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal LoginMember loginMember,
        @PathVariable Long id) {
        favoriteService.deleteFavorite(loginMember, id);
        return ResponseEntity.noContent().build();
    }
}
