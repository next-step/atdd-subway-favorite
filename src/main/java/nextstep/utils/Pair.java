package nextstep.utils;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

public class Pair<T, U> {

    private final T first;
    private final T second;
    private final Repository<T, U> repository;

    public Pair(U first, U second, Repository<T, U> repository) {
        this.repository = repository;
        this.first = findById(first);
        this.second = findById(second);
    }

    public T getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

    private T findById(U id) {
        if (!isCrudRepository()) {
            throw new IllegalArgumentException("Repository is not CrudRepository");
        }
        return ((CrudRepository<T, U>) this.repository).findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    private boolean isCrudRepository() {
        return this.repository instanceof CrudRepository;
    }
}
