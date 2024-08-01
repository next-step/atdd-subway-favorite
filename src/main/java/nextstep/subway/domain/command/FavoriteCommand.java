package nextstep.subway.domain.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class FavoriteCommand {

    @ToString
    @Getter
    @AllArgsConstructor
    public static class CreateFavorite {
        private Long memberId;
        private Long source;
        private Long target;
    }

    @ToString
    @Getter
    @AllArgsConstructor
    public static class DeleteFavorite {
        private Long memberId;
        private Long favoriteId;
    }
}