package nextstep.favorite.ui.dto;

import nextstep.favorite.application.dto.FavoriteCreateCommand;
import nextstep.member.domain.LoginMember;

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

    public static FavoriteCreateCommand toCommand(FavoriteRequest request, LoginMember loginMember) {
        return new FavoriteCreateCommand(request.getSource(), request.getTarget(), loginMember.getEmail());
    }
}
