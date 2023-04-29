package nextstep.favorites.ui;

import lombok.RequiredArgsConstructor;
import nextstep.config.data.UserSession;
import nextstep.favorites.application.FavoritesService;
import nextstep.favorites.application.dto.FavoritesRequest;
import nextstep.favorites.application.dto.FavoritesResponse;
import nextstep.member.application.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoritesController {

    private final MemberService memberService;
    private final FavoritesService favoritesService;

    @PostMapping
    public ResponseEntity<FavoritesResponse> post(@RequestBody FavoritesRequest favoritesRequest, UserSession userSession) {
        memberService.findMember(userSession.getId());
        FavoritesResponse response = favoritesService.save(favoritesRequest);
        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<FavoritesResponse>> get(UserSession userSession) {
        memberService.findMember(userSession.getId());
        List<FavoritesResponse> list = favoritesService.get();
        return ResponseEntity.ok().body(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id, UserSession userSession) {
        memberService.findMember(userSession.getId());
        favoritesService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
