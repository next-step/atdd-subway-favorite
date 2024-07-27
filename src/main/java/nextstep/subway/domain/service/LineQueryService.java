package nextstep.subway.domain.service;

import java.util.List;

import nextstep.subway.application.dto.LineResponse;

public interface LineQueryService {
    LineResponse findLineById(Long id);

    List<LineResponse> findAllLines();
}
