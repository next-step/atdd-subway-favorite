package nextstep.line.application;

import nextstep.line.application.dto.*;

import java.util.List;

public interface LineService {
    LineResponse saveLine(LineCreateRequest lineCreateRequest);

    List<LineResponse> findAllLines();

    LineResponse findLineById(Long id);

    void updateLine(Long id, LineUpdateRequest updateRequest);

    void deleteLine(Long id);

    SectionResponse addSection(Long lineId, SectionCreateRequest createRequest);

    void removeSection(Long lineId, Long stationId);
}
