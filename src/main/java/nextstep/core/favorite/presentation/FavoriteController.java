package nextstep.core.favorite.presentation;

import nextstep.core.favorite.application.FavoriteService;
import nextstep.core.favorite.application.dto.FavoriteRequest;
import nextstep.core.favorite.application.dto.FavoriteResponse;
import nextstep.core.member.application.MemberService;
import nextstep.core.member.domain.LoginMember;
import nextstep.core.member.presentation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final MemberService memberService;

    public FavoriteController(FavoriteService favoriteService, MemberService memberService) {
        this.favoriteService = favoriteService;
        this.memberService = memberService;
    }

    @PostMapping("/favorites")
    public ResponseEntity createFavorite(@RequestBody FavoriteRequest request, @AuthenticationPrincipal LoginMember loginMember) {
        favoriteService.createFavorite(request, memberService.findMemberByEmail(loginMember.getEmail()));
        return ResponseEntity
                .created(URI.create("/favorites/" + 1L))
                .build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> favorites = favoriteService.findFavorites(memberService.findMemberByEmail(loginMember.getEmail()));
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity deleteFavorite(@PathVariable Long id, @AuthenticationPrincipal LoginMember loginMember) {
        favoriteService.deleteFavorite(id, memberService.findMemberByEmail(loginMember.getEmail()));
        return ResponseEntity.noContent().build();
    }
}
