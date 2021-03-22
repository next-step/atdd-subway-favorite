package nextstep.repository;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class MemoryFavoriteRepository implements FavoriteRepository {
    private Map<Long, Favorite> favorites = new HashMap<>();


    @Override
    public Favorite save(Favorite entity) {
        boolean isNew = Objects.isNull(entity.getId());
        if (!isNew) {
            return merge(entity);
        }

        checkDuplicateName(entity);

        long id = favorites.size()+1;
        Favorite newFavorite = new Favorite(id, entity.getMember(), entity.getSource(), entity.getTarget());
        favorites.put(id, newFavorite);
        return newFavorite;
    }

    @Override
    public Optional<Favorite> findById(Long id) {
        return Optional.empty();
    }

    protected void checkDuplicateName(Favorite entity) {
        favorites.values().stream()
                .filter(entity::equals)
                .findFirst()
                .ifPresent(line->{throw new RuntimeException();});
    }

    private Favorite merge(Favorite entity) {
        Favorite favorite = findById(entity.getId()).orElseThrow(IllegalArgumentException::new);
        favorite.update(entity);
        return favorite;
    }
}
