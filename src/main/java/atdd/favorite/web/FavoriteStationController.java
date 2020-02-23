package atdd.favorite.web;

import atdd.favorite.application.FavoriteStationService;
import atdd.favorite.application.dto.CreateFavoriteStationRequestView;
import atdd.favorite.application.dto.FavoriteStationResponseView;
import atdd.favorite.application.dto.FavoriteStationsListView;
import atdd.favorite.domain.FavoriteStation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

import static atdd.Constant.FAVORITE_STATION_BASE_URI;

@RestController
@RequestMapping(FAVORITE_STATION_BASE_URI)
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

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    @GetMapping
    public ResponseEntity<FavoriteStationsListView> showAll(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        List<FavoriteStation> favoriteStations = service.findAllByEmail(email);
        FavoriteStationsListView favoriteStationsListView
                = new FavoriteStationsListView(email, favoriteStations);
        System.out.println(favoriteStations.size());
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(favoriteStationsListView);
    }
}
