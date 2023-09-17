package nextstep.subway.ui;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        MemberResponse memberResponse = memberService.findMemberByEmail(
            userPrincipal.getUsername());
        List<Favorite> favorites = favoriteService.getFavorites(memberResponse.getId());
        return ResponseEntity.ok(FavoriteResponse.listOf(favorites));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FavoriteResponse> getFavorite(@AuthenticationPrincipal UserPrincipal userPrincipal,
        @PathVariable Long id) {
        Favorite favorite = favoriteService.getFavorite(id);
        return ResponseEntity.ok(FavoriteResponse.of(favorite));
    }

    @PostMapping
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal UserPrincipal userPrincipal,
        @RequestBody FavoriteRequest request) {
        MemberResponse memberResponse = memberService.findMemberByEmail(
            userPrincipal.getUsername());
        Favorite favorite = favoriteService.createFavorite(memberResponse.getId(), request);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal UserPrincipal userPrincipal,
        @PathVariable Long id) {
        favoriteService.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }

}
