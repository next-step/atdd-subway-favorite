package nextstep.subway.applicaion.dto;

import nextstep.common.EntitySupplier;
import nextstep.subway.domain.Favorite;

public class FavoriteRequest implements EntitySupplier<Favorite> {
    private Long source;
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
