package nextstep.subway.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nextstep.argumentResolver.Login;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.application.dto.FavoriteRequest;
import nextstep.subway.application.dto.FavoriteResponse;

@RestController
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/favorites")
    public ResponseEntity<FavoriteResponse> createFavorites(@Login MemberResponse member,
        @RequestBody FavoriteRequest request) {
        FavoriteResponse response = favoriteService.createFavorite(member.getId(), request);
        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).build();

    }
}
