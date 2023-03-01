package nextstep.member.application.dto;

import nextstep.subway.applicaion.dto.StationResponse;

import java.util.Objects;
import java.util.StringJoiner;

public class FavoriteResponse {

    private final Long id;
    private final StationResponse source;
    private final StationResponse target;

    public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse make(Long favoriteId, StationResponse source, StationResponse target) {
        return new FavoriteResponse(favoriteId, source, target);
    }

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteResponse that = (FavoriteResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(source, that.source) && Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source, target);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FavoriteResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("source=" + source)
                .add("target=" + target)
                .toString();
    }

}
