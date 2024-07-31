package nextstep.subway.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.subway.domain.command.FavoriteCommand;

@Getter
@AllArgsConstructor
public class CreateFavoriteRequest {
    private Long source;
    private Long target;

    public FavoriteCommand.CreateFavorite toCommand(Long memberId) {
        return new FavoriteCommand.CreateFavorite(memberId, source, target);
    }
}
