package nextstep.subway.fixtures;

import nextstep.subway.domain.Station;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class StationFixtures {

    public static final Long 강남 = 1L;
    public static final Long 양재 = 2L;
    public static final Long 판교 = 3L;

    public static Station 강남역() {
        Station station = new Station("강남");
        setId(station, 강남);

        return station;
    }

    public static Station 양재역() {
        Station station = new Station("양재");
        setId(station, 양재);

        return station;
    }

    public static Station 판교역() {
        Station station = new Station("판교");
        setId(station, 판교);

        return station;
    }

    private static void setId(Station station, Long id) {
        Field idField = ReflectionUtils.findField(station.getClass(), "id");

        ReflectionUtils.makeAccessible(idField);
        ReflectionUtils.setField(idField, station, id);
    }
}
