package nextstep.subway.domain.service;

import nextstep.subway.application.dto.LineRequest;
import nextstep.subway.application.dto.LineResponse;
import nextstep.subway.application.dto.SectionRequest;
import nextstep.subway.application.dto.SectionResponse;

public interface LineCommandService {
    LineResponse saveLine(LineRequest lineRequest);

    void updateLine(Long id, LineRequest lineRequest);

    void deleteLineById(Long id);

    SectionResponse addSection(Long lineId, SectionRequest sectionRequest);

    void removeSection(Long lineId, Long stationId);
}
