package nextstep.subway.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class CustomStationRepositoryImpl implements CustomStationRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Station findStationById(Long id) {
        return Optional.ofNullable(findByIdQuery(id).getSingleResult()).orElseThrow(
                () -> new IllegalArgumentException(SubwayErrorMessage.NOT_FOUND_STATION.getMessage())
        );
    }

    private TypedQuery<Station> findByIdQuery(Long id) {
        String query = "SELECT s FROM Station s WHERE s.id = :id";
        return entityManager.createQuery(query, Station.class).setParameter("id", id);
    }

    @Override
    public List<Station> findStationsByIds(Set<Long> ids) {
        return findByIdsQuery(ids).getResultList();
    }


    private TypedQuery<Station> findByIdsQuery(Set<Long> ids) {
        String query = "SELECT s FROM Station s WHERE s.id IN (:ids)";
        return entityManager.createQuery(query, Station.class).setParameter("ids", ids);
    }
}
