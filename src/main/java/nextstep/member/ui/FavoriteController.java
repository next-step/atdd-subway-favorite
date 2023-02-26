package nextstep.member.ui;

import nextstep.member.application.FavoriteService;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<Void> addFavorite(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
        @RequestBody FavoriteRequest request
        ) {
        String token = JwtTokenProvider.parseToken(authorization);
        favoriteService.createFavorite(token, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findFavorites(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        String token = JwtTokenProvider.parseToken(authorization);
        List<FavoriteResponse> favorites = favoriteService.findFavoritesByToken(token);
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<FavoriteResponse>> deleteFavorite(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
        @PathVariable Long id
    ) {
        String token = JwtTokenProvider.parseToken(authorization);
        favoriteService.deleteFavorite(token, id);
        return ResponseEntity.noContent().build();
    }
}
