package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.exception.InvalidAuthenticationException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.IsExistFavoriteException;
import nextstep.subway.favorite.exception.NotFoundFavoriteException;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final MemberService memberService;

    public FavoriteController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findAllFavoriteOfMine(
        @AuthenticationPrincipal LoginMember loginMember
    ) {
        return ResponseEntity.ok(
            memberService.findAllFavoriteOfMine(loginMember.getId())
        );
    }

    @PostMapping
    public ResponseEntity<Void> addFavorite(
        @AuthenticationPrincipal LoginMember loginMember,
        @RequestBody FavoriteRequest favoriteRequest
    ) {
        Favorite favorite = memberService.addFavorite(loginMember.getId(), favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Void> removeFavorite(
        @AuthenticationPrincipal LoginMember loginMember,
        @PathVariable long favoriteId
    ) {
        memberService.removeFavorite(loginMember.getId(), favoriteId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({ InvalidAuthenticationException.class })
    public ResponseEntity<String> handleInvalidAuthenticationException(
        InvalidAuthenticationException e
    ) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({
        IsExistFavoriteException.class,
        NotFoundFavoriteException.class
    })
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
