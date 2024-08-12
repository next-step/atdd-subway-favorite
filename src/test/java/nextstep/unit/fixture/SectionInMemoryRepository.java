package nextstep.unit.fixture;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import nextstep.line.domain.Section;
import nextstep.line.infrastructure.SectionRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

@SuppressWarnings("NonAsciiCharacters")
public class SectionInMemoryRepository extends PathFixture implements SectionRepository {

    public SectionInMemoryRepository() {
        setUp();
    }

    @Override
    public List<Section> findAll() {
        return List.of(교대역_강남역_구간,
                강남역_양재역_구간,
                교대역_남부터미널_구간,
                남부터미널_양재역_구간);
    }

    @Override
    public List<Section> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public List<Section> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public <S extends Section> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Section> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Section> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Section> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Section getOne(Long aLong) {
        return null;
    }

    @Override
    public Section getById(Long aLong) {
        return null;
    }

    @Override
    public Section getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Section> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Section> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public Page<Section> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Section> S save(S entity) {
        return null;
    }

    @Override
    public Optional<Section> findById(Long aLong) {
        return Optional.empty();
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
    public void delete(Section entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Section> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Section> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Section> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Section> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Section> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Section, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
