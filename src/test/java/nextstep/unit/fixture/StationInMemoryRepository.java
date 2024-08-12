package nextstep.unit.fixture;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import nextstep.station.domain.Station;
import nextstep.station.infrastructure.StationRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

@SuppressWarnings("NonAsciiCharacters")
public class StationInMemoryRepository extends PathFixture implements StationRepository {

    public StationInMemoryRepository() {
        setUp();
    }

    @Override
    public List<Station> findStationsByIdIn(List<Long> ids) {
        return List.of();
    }

    @Override
    public List<Station> findByNameIn(List<String> names) {
        List<Station> stations = findAll();
        return stations.stream()
                .filter(station -> names.contains(station.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Station> findAll() {
        return List.of(교대역, 강남역, 양재역, 남부터미널역);
    }

    @Override
    public List<Station> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public List<Station> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public <S extends Station> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Station> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Station> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Station> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Station getOne(Long aLong) {
        return null;
    }

    @Override
    public Station getById(Long aLong) {
        return null;
    }

    @Override
    public Station getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Station> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Station> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public Page<Station> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Station> S save(S entity) {
        return null;
    }

    @Override
    public Optional<Station> findById(Long id) {
        return Stream.of(교대역, 강남역, 양재역, 남부터미널역).filter(station -> station.getId().equals(id)).findFirst();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Station entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Station> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Station> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Station> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Station> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Station> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Station, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
