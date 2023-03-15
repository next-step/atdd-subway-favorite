package nextstep.subway.ui;

import nextstep.member.application.dto.LoginUser;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<FavoriteResponse> createFavorite(@RequestBody FavoriteRequest request, LoginUser member){

        FavoriteResponse response = favoriteService.create(request, member.getId());

        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).body(response);
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> showFavorites(LoginUser member){
        List<FavoriteResponse> response = favoriteService.showAll(member.getId());

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id){
        favoriteService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
