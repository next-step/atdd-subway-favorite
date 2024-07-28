package nextstep.subway.domain.service;

import nextstep.subway.application.dto.PathResponse;

public interface PathService {
    boolean pathExists(Long sourceId, Long targetId);

    PathResponse findPath(Long sourceId, Long targetId);
}
