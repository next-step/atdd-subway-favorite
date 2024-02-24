package nextstep.core.favorite.presentation;

import nextstep.core.favorite.application.FavoriteService;
import nextstep.core.favorite.application.dto.FavoriteRequest;
import nextstep.core.favorite.application.dto.FavoriteResponse;
import nextstep.core.favorite.domain.Favorite;
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
    public ResponseEntity<Void> createFavorite(@RequestBody FavoriteRequest request,
                                               @AuthenticationPrincipal LoginMember loginMember) {
        Favorite favorite = favoriteService.createFavorite(request, memberService.findMemberByEmail(loginMember.getEmail()));
        return ResponseEntity
                .created(URI.create("/favorites/" + favorite.getId()))
                .build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok().body(favoriteService.findFavorites(memberService.findMemberByEmail(loginMember.getEmail())));
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id,
                                               @AuthenticationPrincipal LoginMember loginMember) {
        favoriteService.deleteFavorite(id, memberService.findMemberByEmail(loginMember.getEmail()));
        return ResponseEntity.noContent().build();
    }
}
