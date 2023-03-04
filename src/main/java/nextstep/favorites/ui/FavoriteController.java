package nextstep.favorites.ui;

import nextstep.favorites.application.FavoriteService;
import nextstep.favorites.application.dto.FavoriteRequest;
import nextstep.favorites.application.dto.FavoriteResponse;
import nextstep.member.application.TokenService;
import nextstep.member.application.dto.MemberResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final TokenService tokenService;

    public FavoriteController(FavoriteService favoriteService, TokenService tokenService) {
        this.favoriteService = favoriteService;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<String> addFavorites(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken, @RequestBody FavoriteRequest favoriteRequest) {
        MemberResponse member = tokenService.getMember(accessToken);
        Long favoriteId = favoriteService.addFavorite(member.getId(), favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteId)).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {
        MemberResponse member = tokenService.getMember(accessToken);
        return ResponseEntity.ok(favoriteService.getFavorites(member.getId()));
    }
}
