package nextstep.subway.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.member.application.dto.LoginMemberRequest;
import nextstep.member.authentication.TokenAuth;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.AddFavoriteRequest;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    ResponseEntity<Void> addFavorite(@TokenAuth LoginMemberRequest loginMemberRequest, @RequestBody AddFavoriteRequest addFavoriteRequest) {
        Long favoriteId = favoriteService.addFavorite(loginMemberRequest.getEmail(), addFavoriteRequest.getSource(), addFavoriteRequest.getTarget());
        return ResponseEntity.created(URI.create("/favorites/" + favoriteId)).build();
    }
}
