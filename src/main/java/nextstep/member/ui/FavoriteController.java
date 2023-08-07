package nextstep.member.ui;

import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.member.application.FavoriteService;
import nextstep.member.application.FavoriteQueryService;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Favorite;
import nextstep.util.ResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final FavoriteQueryService favoriteQueryService;

    public FavoriteController(FavoriteService favoriteService, FavoriteQueryService favoriteQueryService) {
        this.favoriteService = favoriteService;
        this.favoriteQueryService = favoriteQueryService;
    }

    @PostMapping
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody FavoriteRequest favoriteRequest) {
        Favorite favorite = favoriteService.create(userPrincipal, favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findAllFavorite(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<FavoriteResponse> favorites = ResponseMapper.from(favoriteQueryService.find(userPrincipal));
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MemberResponse> deleteMember(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long id) {
        favoriteService.delete(userPrincipal, id);
        return ResponseEntity.noContent().build();
    }
}
