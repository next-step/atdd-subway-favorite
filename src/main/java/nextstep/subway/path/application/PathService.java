package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private LineService lineService;
    private PathFinder pathFinder;

    public PathService(LineService lineService, PathFinder pathFinder) {
        this.lineService = lineService;
        this.pathFinder = pathFinder;
    }

    public PathResponse findPath(LoginMember loginMember, Long source, Long target, PathType type) {
        List<Line> lines = lineService.findLines(loginMember);
        PathResult pathResult = pathFinder.findPath(lines, source, target, type);

        List<Section> sections = pathResult.extractSections(lines);
        List<Station> stations = pathResult.extractStations(lines);
        int duration = sections.stream().mapToInt(it -> it.getDuration()).sum();
        int distance = sections.stream().mapToInt(it -> it.getDistance()).sum();

        return new PathResponse(stations, duration, distance);
    }
}
