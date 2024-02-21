package nextstep.subway.line.service;

import nextstep.subway.line.LineRepository;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.exception.LineException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class LineDataService {

    private final LineRepository lineRepository;

    public LineDataService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public Line findLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LineException("존재하지 않는 노선입니다."));
    }
}
