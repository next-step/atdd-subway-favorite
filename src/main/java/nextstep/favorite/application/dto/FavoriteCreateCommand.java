package nextstep.favorite.application.dto;

public class FavoriteCreateCommand {
    private Long source;
    private Long target;
    private String memberEmail;

    public FavoriteCreateCommand() {
    }

    public FavoriteCreateCommand(Long source, Long target, String memberEmail) {
        this.source = source;
        this.target = target;
        this.memberEmail = memberEmail;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public String getMemberEmail() {
        return memberEmail;
    }
}
