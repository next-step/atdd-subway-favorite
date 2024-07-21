package nextstep.subway.domain.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class LineCommand {
    @ToString
    @Getter
    @AllArgsConstructor
    public static class CreateLine {
        private String name;
        private String color;
        private Long upStationId;
        private Long downStationId;
        private Long distance;
    }

    @ToString
    @Getter
    @AllArgsConstructor
    public static class UpdateLine {
        private Long id;
        private String name;
        private String color;
    }

    @ToString
    @Getter
    @AllArgsConstructor
    public static class AddSection {
        private Long lineId;
        private Long upStationId;
        private Long downStationId;
        private Long distance;
    }

    @ToString
    @Getter
    @AllArgsConstructor
    public static class DeleteSection {
        private Long lineId;
        private Long stationId;
    }
}
