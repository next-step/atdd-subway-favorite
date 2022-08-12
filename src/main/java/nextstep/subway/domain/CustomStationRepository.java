package nextstep.subway.domain;

import java.util.List;
import java.util.Set;

public interface CustomStationRepository {

    Station findStationById(Long id);

    List<Station> findStationsByIds(Set<Long> ids);

}
