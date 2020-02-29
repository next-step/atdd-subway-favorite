package atdd.path.application;

import atdd.path.application.dto.FavoriteStationResponse;
import atdd.path.dao.FavoriteStationDao;
import atdd.path.dao.StationDao;
import atdd.user.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = FavoriteStationService.class)
public class FavoriteStationServiceTest {
    @Autowired
    private FavoriteStationService favoriteStationService;

    @MockBean
    private FavoriteStationDao favoriteStationDao;
    @MockBean
    private UserDao userDao;
    @MockBean
    private StationDao stationDao;

    @Test
    public void addFavoriteStation() {
        given(userDao.findByEmail(any())).willReturn(TEST_USER_2);
        given(stationDao.findById(TEST_STATION.getId())).willReturn(TEST_STATION);
        given(favoriteStationDao.save(any())).willReturn(FAVORITE_STATION_1);

        FavoriteStationResponse response = favoriteStationService.addFavoriteStation(USER_EMAIL2, TEST_STATION.getId());

        assertThat(response.getStation().getId()).isEqualTo(TEST_STATION.getId());
    }

    @Test
    public void findAll() {
        given(userDao.findByEmail(any())).willReturn(TEST_USER_2);
        given(favoriteStationDao.findAllByOwner(TEST_USER_2.getId())).willReturn(Arrays.asList(FAVORITE_STATION_1));
        given(stationDao.findById(TEST_STATION.getId())).willReturn(TEST_STATION);

        List<FavoriteStationResponse> responses = favoriteStationService.findAll(USER_EMAIL2);

        assertThat(responses.size()).isEqualTo(1);
        assertThat(responses.get(0).getStation().getId()).isEqualTo(TEST_STATION.getId());
    }
}
