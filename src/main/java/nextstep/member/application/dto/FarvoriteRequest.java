package nextstep.member.application.dto;

public class FarvoriteRequest {
    private Long source;
    private Long target;

    public FarvoriteRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
