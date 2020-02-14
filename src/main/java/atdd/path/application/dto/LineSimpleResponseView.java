package atdd.path.application.dto;

import atdd.path.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

public class LineSimpleResponseView {
    private Long id;
    private String name;

    public LineSimpleResponseView() {
    }

    public LineSimpleResponseView(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<LineSimpleResponseView> listOf(List<Line> lines) {
        return lines.stream()
                .map(it -> LineSimpleResponseView.of(it))
                .collect(Collectors.toList());
    }

    private static LineSimpleResponseView of(Line line) {
        return new LineSimpleResponseView(line.getId(), line.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
