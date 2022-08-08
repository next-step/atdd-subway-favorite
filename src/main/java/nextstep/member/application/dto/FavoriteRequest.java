package nextstep.member.application.dto;

import nextstep.common.EntitySupplier;
import nextstep.member.domain.Favorite;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class FavoriteRequest implements EntitySupplier<Favorite> {

    @NotNull
    @Min(1)
    private Long source;

    @NotNull
    @Min(1)
    private Long target;

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    @Override
    public Favorite toEntity() {
        return new Favorite(source, target);
    }
}
