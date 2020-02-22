package atdd.bookmark.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import atdd.bookmark.entity.Bookmark;

public interface BookmarkRepository extends CrudRepository<Bookmark, Long> {
  List<Bookmark> findByUserIDAndTargetStationIdIsNull(Long userID);
}
