package nextstep.member.application.dto;

public class CreateFavoriteRequest {
    private Long source;
    private Long target;

    public CreateFavoriteRequest() {
    }

    public CreateFavoriteRequest(Long source, Long target) {
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
