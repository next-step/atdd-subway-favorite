package nextstep.favorite.application.dto;

import nextstep.line.exception.CreateRequestNotValidException;

import java.util.Objects;

public class FavoriteRequest {
    private Long source;
    private Long target;

    public FavoriteRequest() {
    }

    public FavoriteRequest(final Long source, final Long target) {
        this.source = source;
        this.target = target;
        validate();
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public void validate() {
        if (Objects.isNull(source)) {
            throw new CreateRequestNotValidException("source can not be null");
        }
        if (Objects.isNull(target)) {
            throw new CreateRequestNotValidException("target can not be null");
        }
    }
}
