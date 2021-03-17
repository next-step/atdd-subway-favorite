package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;

import java.util.function.Function;

public enum PathType {
    DISTANCE(section -> section.getDistance()),
    DURATION(section -> section.getDuration());

    private Function<Section, Integer> expression;

    PathType(Function<Section, Integer> expression) {
        this.expression = expression;
    }

    public int findWeightOf(Section section) {
        return expression.apply(section);
    }
}
