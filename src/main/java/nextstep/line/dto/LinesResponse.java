package nextstep.line.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LinesResponse {

    private List<LineResponse> lineResponses = new ArrayList<>();

    public LinesResponse() {
    }

    public LinesResponse(List<LineResponse> lineResponse) {
        this.lineResponses = lineResponse;
    }

    public void addLineResponse(final LineResponse lineResponse) {
        this.lineResponses.add(lineResponse);
    }

    public List<LineResponse> getLineResponses() {
        return this.lineResponses;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        LinesResponse that = (LinesResponse) obj;
        return Objects.equals(lineResponses, that.lineResponses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineResponses);
    }
}

