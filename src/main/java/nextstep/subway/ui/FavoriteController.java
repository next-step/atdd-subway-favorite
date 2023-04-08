package nextstep.subway.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).body(response);
    }

    @GetMapping("/favorites/{favoriteId}")
    public ResponseEntity<FavoriteResponse> getFavorites(@Login MemberResponse member,
        @PathVariable(value = "favoriteId") long favoriteId) {
        FavoriteResponse response = favoriteService.getFavorite(member.getId(), favoriteId);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/favorites/{favoriteId}")
    public ResponseEntity<FavoriteResponse> deleteFavorites(@Login MemberResponse member,
        @PathVariable(value = "favoriteId") long favoriteId) {
        favoriteService.deleteFavorite(member.getId(), favoriteId);
        return ResponseEntity.noContent().build();
    }
}
