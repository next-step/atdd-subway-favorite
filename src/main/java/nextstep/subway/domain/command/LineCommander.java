package nextstep.subway.domain.command;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.repository.LineRepository;
import nextstep.subway.domain.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nextstep.subway.domain.entity.line.Line;

@Service
@RequiredArgsConstructor
public class LineCommander {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional
    public Long createLine(LineCommand.CreateLine command) {
        verifyStationExist(command.getUpStationId(), command.getDownStationId());
        Line line = lineRepository.save(Line.init(command));
        return line.getId();
    }

    @Transactional
    public void updateLine(LineCommand.UpdateLine command) {
        Line line = lineRepository.findByIdOrThrow(command.getId());
        line.update(command);
        lineRepository.save(line);
    }

    @Transactional
    public void deleteLineById(Long id) {
        Line line = lineRepository.findByIdOrThrow(id);
        lineRepository.delete(line);
    }

    @Transactional
    public void addSection(LineCommand.AddSection command) {
        Line line = lineRepository.findByIdOrThrow(command.getLineId());
        verifyStationExist(command.getUpStationId(), command.getDownStationId());
        line.addSection(command.getUpStationId(), command.getDownStationId(), command.getDistance());
        lineRepository.save(line);
    }

    @Transactional
    public void deleteSection(LineCommand.DeleteSection command) {
        Line line = lineRepository.findByIdOrThrow(command.getLineId());
        line.deleteSection(command.getStationId());
        lineRepository.save(line);
    }

    private void verifyStationExist(Long upStationId, Long downStationId) {
        this.stationRepository.findByIdOrThrow(upStationId);
        this.stationRepository.findByIdOrThrow(downStationId);
    }
}
