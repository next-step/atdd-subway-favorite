package atdd.favorite.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/favorite-stations")
public class FavoriteStationController {

    @PostMapping
    public void create(){

    }
}
