package nextstep.favorite.ui;

import java.net.URI;
import java.util.List;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.member.application.dto.LoginMemberRequest;
import nextstep.member.ui.argumentresolver.LoginMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/favorites")
@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity register(
            @LoginMember LoginMemberRequest loginMemberRequest,
            @RequestBody FavoriteRequest favoriteRequest
    ) {
        Long id = favoriteService.register(loginMemberRequest, favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<List> show(@LoginMember LoginMemberRequest loginMemberRequest) {
        return ResponseEntity.ok().body(favoriteService.getFavorites(loginMemberRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@LoginMember LoginMemberRequest loginMemberRequest, @PathVariable Long id) {
        favoriteService.delete(loginMemberRequest, id);
        return ResponseEntity.noContent().build();
    }
}
