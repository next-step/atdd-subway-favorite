package nextstep.favorite.ui.dto;

import nextstep.favorite.application.dto.FavoriteCreateCommand;
import nextstep.member.domain.Member;

public class FavoriteRequest {
    private Long source;
    private Long target;

    public FavoriteRequest() {
    }

    public FavoriteRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public static FavoriteCreateCommand toCommand(Long source, Long target, Member member) {
        return new FavoriteCreateCommand(source, target, member.getId());
    }
}
