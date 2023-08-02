package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.StationData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationResponseRepository extends JpaRepository<StationData, Long> {

}
