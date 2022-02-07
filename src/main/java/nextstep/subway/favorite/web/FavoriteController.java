package nextstep.subway.favorite.web;

import lombok.RequiredArgsConstructor;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.service.FavoriteService;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

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
}
