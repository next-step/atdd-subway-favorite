package nextstep.favorite.ui;

import lombok.RequiredArgsConstructor;
import nextstep.member.MemberNotFoundException;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.ui.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    private final MemberRepository memberRepository;

    @PostMapping("/favorites")
    public ResponseEntity createFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                         @RequestBody FavoriteRequest request) {
        Member member = memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(MemberNotFoundException::new);

        Long favoriteId = favoriteService.createFavorite(request, member);

        return ResponseEntity
                .created(URI.create("/favorites/" + favoriteId))
                .build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        Member member = memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(MemberNotFoundException::new);

        List<FavoriteResponse> favorites = favoriteService.findFavorites(member);
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity deleteFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                         @PathVariable Long id) {
        Member member = memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(MemberNotFoundException::new);

        favoriteService.deleteFavorite(id, member);
        return ResponseEntity.noContent().build();
    }
}
