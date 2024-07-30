package nextstep.favorite.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.favorite.domain.command.FavoriteCommand;

@Getter
@AllArgsConstructor
public class FavoriteRequest {
    private Long source;
    private Long target;

    public FavoriteRequest() {
    }

    public FavoriteCommand.CreateFavorite toCommand() {
        return new FavoriteCommand.CreateFavorite(source, target);
    }
}
