package atdd.favorite.web;

import atdd.favorite.application.FavoriteStationService;
import atdd.favorite.application.dto.CreateFavoriteStationRequestView;
import atdd.favorite.application.dto.FavoriteStationResponseView;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

import static atdd.Constant.FAVORITE_STATION_BASE_URI;

@RestController
@RequestMapping(value = FAVORITE_STATION_BASE_URI,  consumes = MediaType.APPLICATION_JSON_VALUE)
public class FavoriteStationController {
    private FavoriteStationService service;

    private FavoriteStationController(FavoriteStationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<FavoriteStationResponseView> createFavoriteStation(@RequestBody CreateFavoriteStationRequestView createRequestView,
                                                                             HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        createRequestView.insertUserEmail(email);
        FavoriteStationResponseView response = service.createFavoriteStation(createRequestView);
        return ResponseEntity
                .created(URI.create(FAVORITE_STATION_BASE_URI + "/" + response.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
