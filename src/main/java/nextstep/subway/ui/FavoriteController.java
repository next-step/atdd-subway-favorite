package nextstep.subway.ui;

import io.jsonwebtoken.Header;
import lombok.RequiredArgsConstructor;
import nextstep.config.annotation.AuthHeader;
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

    /*
        argument resolver를 이용해 내정보를 조회하는게 좋을까?
        가독성은 좋으나 회원을 조회하는 argument resolver를 회원패키지에 추가하면 서로다른 패키지를 참조하게 된다.
        헤더에서 내정보를 조회하는더 좋은 방법이 있을까??
     */
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid FavoriteCreateRequest request, @AuthHeader String header) {
        Favorite favorite = favoriteService.save(request, header);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @GetMapping("{id}")
    public void get(@PathVariable Long id) {

    }
}

