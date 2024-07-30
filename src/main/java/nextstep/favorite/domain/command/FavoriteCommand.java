package nextstep.favorite.domain.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class FavoriteCommand {

    @ToString
    @Getter
    @AllArgsConstructor
    public static class CreateFavorite {
        private Long source;
        private Long target;
    }
}