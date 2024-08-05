package nextstep.subway.utils;

import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.section.Sections;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.dto.StationResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LineUtil {
    public static <T> List<String> getStationNames(List<T> list) {
        if (!list.isEmpty() && list.get(0) instanceof StationResponse) {
            return list.stream()
                    .map(item -> ((StationResponse) item).getName())
                    .distinct()
                    .collect(Collectors.toList());
        }

        if (!list.isEmpty() && list.get(0) instanceof Section) {
            return list.stream()
                    .flatMap(section -> Stream.of(((Section) section).getUpwardStation().getName(), ((Section) section).getDownwardStation().getName()))
                    .distinct()
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
