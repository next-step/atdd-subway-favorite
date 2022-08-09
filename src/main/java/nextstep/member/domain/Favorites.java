package nextstep.member.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Favorites {

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<Favorite> value = new ArrayList<>();

    public void addFavorite(Favorite favorite) {
        checkDuplicateFavorite(favorite);
        value.add(favorite);
    }

    private void checkDuplicateFavorite(Favorite favorite) {
        value.stream().filter(f -> f.equals(favorite))
             .findAny()
             .ifPresent(f -> {
                    throw  new IllegalArgumentException("이미 등록된 즐겨찾기 입니다.");
                });
    }

}
