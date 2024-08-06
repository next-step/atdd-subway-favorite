package nextstep.favorite.payload;

import nextstep.exceptions.ValidationException;

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

    public void validate() {
        if(target.equals(source)) {
            throw new ValidationException("두개의 역은 같은 역이 아니어야합니다");
        }
    }
}
