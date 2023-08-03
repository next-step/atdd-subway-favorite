package nextstep.subway.ui;

import java.net.URI;
import java.util.List;
import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.member.application.MemberService;
import nextstep.subway.applicaion.FavoritesService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoritesResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavoriteController {

    private final FavoritesService favoritesService;
    private final MemberService memberService;

    public FavoriteController(FavoritesService favoritesService, MemberService memberService) {
        this.favoritesService = favoritesService;
        this.memberService = memberService;
    }

    @PostMapping("/favorites")
    ResponseEntity<Object> addFavorites(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @RequestBody FavoriteRequest request) {

        return ResponseEntity
            .created(URI.create(
                "/favorites/" + favoritesService.create(request, getMemberId(userPrincipal))))
            .build();
    }

    private Long getMemberId(UserPrincipal userPrincipal) {
        return memberService
            .findMemberByEmail(userPrincipal.getUsername())
            .getId();
    }

    @GetMapping("/favorites")
    ResponseEntity<List<FavoritesResponse>> getFavorite(
        @AuthenticationPrincipal UserPrincipal userPrincipal) {

        return ResponseEntity.ok(
            favoritesService.findAllByUser(getMemberId(userPrincipal))
        );
    }

    @DeleteMapping("/favorites/{id}")
    ResponseEntity<Object> deleteFavorites(
        @PathVariable(value = "id") long favoritesId,
        @AuthenticationPrincipal UserPrincipal userPrincipal) {

        favoritesService.delete(favoritesId, getMemberId(userPrincipal));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
