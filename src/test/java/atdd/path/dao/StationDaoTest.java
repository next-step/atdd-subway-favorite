package atdd.path.dao;

import atdd.path.application.exception.NoDataException;
import atdd.path.domain.Edge;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

import static atdd.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class StationDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;
    private LineDao lineDao;
    private StationDao stationDao;
    private EdgeDao edgeDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
        lineDao.setDataSource(dataSource);
        stationDao = new StationDao(jdbcTemplate);
        stationDao.setDataSource(dataSource);
        edgeDao = new EdgeDao(jdbcTemplate);
        edgeDao.setDataSource(dataSource);
    }

    @Test
    public void save() {
        Station persistStation = stationDao.save(TEST_STATION);
        assertThat(persistStation.getId()).isNotNull();
        assertThat(persistStation.getName()).isEqualTo(STATION_NAME);
    }

    @Test
    public void saveSameName() {
        stationDao.save(TEST_STATION);

        Assertions.assertThrows(
                DuplicateKeyException.class,
                () -> stationDao.save(TEST_STATION));
    }

    @Test
    public void findById() {
        Station savedStation = stationDao.save(TEST_STATION);

        Station persistStation = stationDao.findById(savedStation.getId());

        assertThat(persistStation.getId()).isNotNull();
        assertThat(persistStation.getName()).isEqualTo(STATION_NAME);
    }

    @Test
    public void findByIdWithoutStation() {
        Assertions.assertThrows(
                NoDataException.class,
                () -> stationDao.findById(STATION_ID));

    }

    @Test
    public void findByIdWithLine() {
        Station station1 = stationDao.save(TEST_STATION);
        Station station2 = stationDao.save(TEST_STATION_2);
        Station station3 = stationDao.save(TEST_STATION_3);
        Station station4 = stationDao.save(TEST_STATION_4);
        Line savedLine = lineDao.save(TEST_LINE);
        Line savedLine2 = lineDao.save(TEST_LINE_2);
        int distance = 10;
        edgeDao.save(savedLine.getId(), Edge.of(station1, station2, distance));
        edgeDao.save(savedLine.getId(), Edge.of(station2, station3, distance));
        edgeDao.save(savedLine2.getId(), Edge.of(station3, station4, distance));
        edgeDao.save(savedLine2.getId(), Edge.of(station4, station1, distance));

        Station persistStation = stationDao.findById(station1.getId());
        assertThat(persistStation.getId()).isEqualTo(station1.getId());
        assertThat(persistStation.getName()).isEqualTo(station1.getName());
        assertThat(persistStation.getLines().size()).isEqualTo(2);
    }

    @Test
    public void findAll() {
        stationDao.save(TEST_STATION);
        stationDao.save(TEST_STATION_2);
        stationDao.save(TEST_STATION_3);

        List<Station> persistStations = stationDao.findAll();

        assertThat(persistStations.size()).isEqualTo(3);
    }

    @Test
    public void findAllWhenEmpty() {
        List<Station> persistStations = stationDao.findAll();

        assertThat(persistStations.size()).isEqualTo(0);
    }

    @Test
    public void deleteById() {
        Station persisStation = stationDao.save(TEST_STATION);

        stationDao.deleteById(persisStation.getId());

        Assertions.assertThrows(
                NoDataException.class,
                () -> stationDao.findById(persisStation.getId())
        );
    }
}
