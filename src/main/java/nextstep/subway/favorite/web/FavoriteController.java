package nextstep.subway.favorite.web;

import lombok.RequiredArgsConstructor;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.service.FavoriteService;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/favorites")
@Controller
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal LoginMember member,
                                               @RequestBody FavoriteRequest request) {
        Long favoriteId = favoriteService.create(member.getId(), request);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteId)).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> showFavorites(@AuthenticationPrincipal LoginMember member) {
        List<FavoriteResponse> favorites = favoriteService.findAllByMemberId(member.getId());
        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal LoginMember member,
                                               @PathVariable("id") long favoriteId) {
        return ResponseEntity.noContent().build();
    }

}
