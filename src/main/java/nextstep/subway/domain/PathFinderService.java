package nextstep.subway.domain;

public interface PathFinderService {
    boolean isValidPath(Long source, Long target);

    PathResult findPath(Long source, Long target);
}
