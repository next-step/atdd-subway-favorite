package nextstep.subway.controller;

import lombok.RequiredArgsConstructor;
import nextstep.configuration.auth.AuthenticationPrincipal;
import nextstep.member.controller.dto.LoginMember;
import nextstep.member.domain.entity.Member;
import nextstep.member.domain.query.MemberReader;
import nextstep.subway.controller.dto.CreateFavoriteRequest;
import nextstep.subway.domain.command.FavoriteCommander;
import nextstep.subway.domain.query.FavoriteReader;
import nextstep.subway.domain.view.FavoriteView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteCommander favoriteCommander;
    private final FavoriteReader favoriteReader;
    private final MemberReader memberReader;

    @PostMapping("")
    public ResponseEntity<FavoriteView.Main> createFavorite(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestBody CreateFavoriteRequest request
    ) {
        Member member = memberReader.getMe(loginMember.getEmail());
        Long id = favoriteCommander.createFavorite(request.toCommand(member.getId()));

        FavoriteView.Main view = favoriteReader.getById(id);

        return ResponseEntity
                .created(URI.create("/favorites/" + id))
                .body(view);
    }

    @GetMapping("")
    public ResponseEntity<List<FavoriteView.Main>> getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        Member member = memberReader.getMe(loginMember.getEmail());

        List<FavoriteView.Main> favorites = favoriteReader.getFavoritesByMemberId(member.getId());
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id) {
        favoriteCommander.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }
}
