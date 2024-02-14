package nextstep.api.favorite.application.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteCreateRequest {
    private Long id;
    private Long sourceStationId;
    private Long targetStationId;
    private Long memberId;

    public FavoriteCreateRequest withMemberId(Long memberId) {
        this.memberId = memberId;
        return this;
    }
}
