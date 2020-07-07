package nextstep.subway.path.domain;

import nextstep.subway.line.dto.LineStationResponse;

import java.util.function.Function;

public enum PathType {
    DISTANCE(lineStation -> lineStation.getDistance()),
    DURATION(lineStation -> lineStation.getDuration());

    private Function<LineStationResponse, Integer> expression;

    PathType(Function<LineStationResponse, Integer> expression) {
        this.expression = expression;
    }

    public int findWeightOf(LineStationResponse lineStation) {
        return expression.apply(lineStation);
    }
}
