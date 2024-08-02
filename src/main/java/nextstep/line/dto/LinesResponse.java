package nextstep.line.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LinesResponse {

    private List<LineResponse> lineResponseList = new ArrayList<>();

    public LinesResponse() {
    }

    public LinesResponse(List<LineResponse> lineResponse) {
        this.lineResponseList = lineResponse;
    }

    public void addLineResponse(LineResponse lineResponse) {
        this.lineResponseList.add(lineResponse);
    }

    public List<LineResponse> getLineResponseList() {
        return this.lineResponseList;
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
        return Objects.equals(lineResponseList, that.lineResponseList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineResponseList);
    }
}
