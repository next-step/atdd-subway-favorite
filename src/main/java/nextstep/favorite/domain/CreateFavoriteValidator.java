package nextstep.favorite.domain;

import java.util.Collection;
import nextstep.subway.applicaion.DijkstraPathFinder;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.ui.BusinessException;

public class CreateFavoriteValidator {

  public static void validate(
      final Station source,
      final Station target,
      final Collection<Section> sections
  ) {
    if (source.equals(target)) {
      throw new BusinessException("출발역과 도착역을 다르게 설정해주세요.");
    }

    final var pathFinder = new DijkstraPathFinder(sections);
    if (!pathFinder.isPathExists(source, target)) {
      throw new BusinessException("이어지지 않는 경로입니다.");
    }
  }

}
