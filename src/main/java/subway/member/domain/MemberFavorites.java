package subway.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.exception.SubwayBadRequestException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Embeddable
@NoArgsConstructor
public class MemberFavorites {

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<Favorite> favorites = new ArrayList<>();

    public void add(Favorite newFavorite, Member member) {
        validAlreadyExist(newFavorite);

        newFavorite.setMember(member);
        add(newFavorite);
    }

    public boolean IsExistFavorite(Favorite favorite) {
        return favorites.contains(favorite);
    }

    public void removeFavorite(Member member, Favorite favorite) {
        if (!member.IsExistFavorite(favorite)) {
            throw new SubwayBadRequestException(9999L, "내가 소유한 즐겨찾기가 아닙니다.");// TODO : constant
        }
        remove(favorite);
    }

    private void validAlreadyExist(Favorite newFavorite) {
        Optional<Favorite> findAnyFavorite = findFavoriteBySourceAndTarget(newFavorite);
        if (findAnyFavorite.isPresent()) {
            throw new SubwayBadRequestException(9999L, "이미 같은 경로의 즐겨찾기가 존재합니다."); // TODO : constant
        }

    }

    private Optional<Favorite> findFavoriteBySourceAndTarget(Favorite newFavorite) {
        return favorites.stream()
                .filter(favorite -> favorite.getSourceStation().equals(newFavorite.getSourceStation()) || favorite.getTargetStation().equals(newFavorite.getTargetStation()))
                .findAny();
    }

    private void add(Favorite newFavorite) {
        this.favorites.add(newFavorite);
    }

    private void remove(Favorite favorite) {
        this.favorites.remove(favorite);
    }
}
