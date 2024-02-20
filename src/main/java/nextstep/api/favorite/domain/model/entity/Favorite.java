package nextstep.api.favorite.domain.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextstep.api.favorite.domain.model.dto.inport.FavoriteCreateCommand;
import nextstep.common.mapper.ModelMapperBasedObjectMapper;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;
    private Long sourceStationId;
    private Long targetStationId;

    public static Favorite from(FavoriteCreateCommand request) {
        return ModelMapperBasedObjectMapper.convert(request, Favorite.class);
    }

    public boolean isNotBelongTo(Long memberId){
        return !isBelongTo(memberId);
    }

    public boolean isBelongTo(Long memberId) {
        return this.memberId !=null && this.memberId.equals(memberId);
    }
}
