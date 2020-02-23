package atdd.path.application.dto.favorite;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class FavoriteCreateRequestView {
    private String name;

    public FavoriteCreateRequestView(String name) {
        this.name = name;
    }
}
