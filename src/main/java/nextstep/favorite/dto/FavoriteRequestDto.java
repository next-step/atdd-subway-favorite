package nextstep.favorite.dto;

public class FavoriteRequestDto {
    private final Long source;
    private final Long target;

    public FavoriteRequestDto(Long source, Long target) {
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
