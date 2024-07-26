package nextstep.subway.dto;

public class PathRequest {
    private Long source;
    private Long target;

    public Long getSource() {
        return source;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public Long getTarget() {
        return target;
    }

    public void setTarget(Long target) {
        this.target = target;
    }
}
