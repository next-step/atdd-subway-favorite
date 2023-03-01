package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.auth.annotation.MyInfo;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteCreateRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid FavoriteCreateRequest request, @MyInfo Member member) {
        request.addMember(member);
        Favorite favorite = favoriteService.save(request);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @GetMapping("{favoriteId}")
    public FavoriteResponse findMyFavoriteById(@PathVariable Long favoriteId, @MyInfo Member member) {
        return favoriteService.findMyFavoriteById(favoriteId, member.getId());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id, @MyInfo Member member) {
        favoriteService.deleteMyFavoriteById(id, member.getId());
        return ResponseEntity.noContent().build();
    }
}

