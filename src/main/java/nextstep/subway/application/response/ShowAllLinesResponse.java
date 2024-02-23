package nextstep.subway.application.response;

import nextstep.subway.application.dto.ShowLineDto;

import java.util.List;

public class ShowAllLinesResponse {

    private List<ShowLineDto> lines;

    private ShowAllLinesResponse() {
    }

    private ShowAllLinesResponse(List<ShowLineDto> lineDtos) {
        this.lines = lineDtos;
    }

    public static ShowAllLinesResponse of(List<ShowLineDto> lineDtos) {
        return new ShowAllLinesResponse(lineDtos);
    }

    public List<ShowLineDto> getLines() {
        return lines;
    }

}
