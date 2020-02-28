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
public class LineDaoTest {
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
        Line persistLine = lineDao.save(TEST_LINE);

        assertThat(persistLine.getId()).isNotNull();
        assertThat(persistLine.getName()).isEqualTo(LINE_NAME);
    }

    @Test
    public void saveSameName() {
        lineDao.save(TEST_LINE);

        Assertions.assertThrows(
                DuplicateKeyException.class,
                () -> lineDao.save(TEST_LINE));
    }

    @Test
    public void findById() {
        Line savedLine = lineDao.save(TEST_LINE);

        Line persistLine = lineDao.findById(savedLine.getId());

        assertThat(persistLine.getId()).isNotNull();
        assertThat(persistLine.getName()).isEqualTo(LINE_NAME);
    }

    @Test
    public void findByIdWithEdges() {
        Station station1 = stationDao.save(TEST_STATION);
        Station station2 = stationDao.save(TEST_STATION_2);
        Station station3 = stationDao.save(TEST_STATION_3);
        Station station4 = stationDao.save(TEST_STATION_4);
        Line savedLine = lineDao.save(TEST_LINE);
        int distance = 10;
        edgeDao.save(savedLine.getId(), Edge.of(station1, station2, distance));
        edgeDao.save(savedLine.getId(), Edge.of(station2, station3, distance));
        edgeDao.save(savedLine.getId(), Edge.of(station3, station4, distance));

        Line persistLine = lineDao.findById(savedLine.getId());

        assertThat(persistLine.getId()).isNotNull();
        assertThat(persistLine.getName()).isEqualTo(LINE_NAME);
        assertThat(persistLine.getEdges().size()).isEqualTo(3);
    }

    @Test
    public void findByIdWithoutLine() {
        Assertions.assertThrows(
                NoDataException.class,
                () -> lineDao.findById(LINE_ID));

    }

    @Test
    public void findAll() {
        lineDao.save(TEST_LINE);
        lineDao.save(TEST_LINE_2);
        lineDao.save(TEST_LINE_3);

        List<Line> persistLines = lineDao.findAll();

        assertThat(persistLines.size()).isEqualTo(3);
    }

    @Test
    public void findAllWithEdges() {
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

        List<Line> persistLines = lineDao.findAll();

        assertThat(persistLines.size()).isEqualTo(2);
    }

    @Test
    public void findAllWhenEmpty() {
        List<Line> persistLines = lineDao.findAll();

        assertThat(persistLines.size()).isEqualTo(0);
    }

    @Test
    public void deleteById() {
        Line persisLine = lineDao.save(TEST_LINE);

        lineDao.deleteById(persisLine.getId());

        Assertions.assertThrows(
                NoDataException.class,
                () -> lineDao.findById(persisLine.getId())
        );
    }
}
