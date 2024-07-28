package nextstep.favorite.infra;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.Member;

public class InMemoryFavoriteRepository implements FavoriteRepository {
    private final HashMap<Long, Favorite> favorites = new HashMap<>();

    @Override
    public Optional<Favorite> findById(Long id) {
        return Optional.ofNullable(favorites.get(id));
    }

    @Override
    public List<Favorite> findByMember(Member member) {
        return favorites.values()
            .stream()
            .filter(favorite -> favorite.matchesMemberEmail(member.getEmail()))
            .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        favorites.remove(id);
    }

    @Override
    public Favorite save(Favorite favorite) {
        return favorites.put(favorite.getId(), favorite);
    }
}
