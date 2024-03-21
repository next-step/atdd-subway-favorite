package nextstep.favorite.ui.dto;

import nextstep.auth.domain.LoginUserDetail;
import nextstep.favorite.application.dto.FavoriteCreateCommand;

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

    public static FavoriteCreateCommand toCommand(FavoriteRequest request, LoginUserDetail loginUserDetail) {
        return new FavoriteCreateCommand(request.getSource(), request.getTarget(), loginUserDetail.getEmail());
    }
}
