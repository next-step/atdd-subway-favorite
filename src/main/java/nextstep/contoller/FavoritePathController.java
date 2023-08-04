package nextstep.contoller;

import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.domain.FavoritePath;
import nextstep.domain.member.Member;
import nextstep.dto.FavoritePathRequest;
import nextstep.dto.FavoritePathResponse;
import nextstep.service.FavoritePathService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoritePathController {
    private final FavoritePathService favoritePathService;

    public FavoritePathController(FavoritePathService favoritePathService) {
        this.favoritePathService = favoritePathService;
    }

    @PostMapping
    public ResponseEntity<FavoritePathResponse> createFavorite(@RequestBody FavoritePathRequest favoritePathRequest, @AuthenticationPrincipal Member member) {
        FavoritePath favoritePath = favoritePathService.createFavoritePath(favoritePathRequest, member);
        return ResponseEntity
                .created(URI.create("/favorites/" + favoritePath.getId()))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<FavoritePathResponse>> getFavoritePaths(@AuthenticationPrincipal Member member){
        List<FavoritePathResponse> responseList = favoritePathService.findAllFavoritePaths(member);

        return ResponseEntity.ok(responseList);
    }

    @DeleteMapping("/{favoritePathId}")
    public ResponseEntity<Void> deleteFavoritePath(@PathVariable Long favoritePathId,@AuthenticationPrincipal Member member){
        favoritePathService.deleteFavoritePath(favoritePathId,member);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
