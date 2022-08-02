package nextstep.member.ui;

import nextstep.auth.authentication.AuthenticationPrincipal;
import nextstep.auth.secured.Secured;
import nextstep.auth.service.LoginMember;
import nextstep.member.application.FavoriteService;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FavoriteController {

    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    @Secured("ROLE_MEMBER")
    public ResponseEntity<Void> addFavorite(@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest favoriteRequest) {
        favoriteService.createFavorite(loginMember.getEmail(), favoriteRequest.getSource(), favoriteRequest.getTarget());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> responses = favoriteService.getFavorites(loginMember.getEmail());
        return ResponseEntity.ok().body(responses);
    }
}