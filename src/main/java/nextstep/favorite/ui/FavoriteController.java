package nextstep.favorite.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/favorites", produces = MediaType.APPLICATION_JSON_VALUE)
public class FavoriteController {

}
