package nextstep.subway.line.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<LineStationsResponse> stations;

    public LineResponse(Line line, List<LineStationsResponse> stations) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.stations = stations;
    }

    public static LineResponse from(Line line) {
        List<Section> sections = line.getSections();

        List<LineStationsResponse> stationList = sections.stream()
                .flatMap(section -> Stream.of(
                                new LineStationsResponse(section.getUpStation()),
                                new LineStationsResponse(section.getDownStation())
                        )
                )
                .distinct()
                .collect(Collectors.toList());

        return new LineResponse(line, stationList);
    }
}
