package atdd.bookmark.application.dto;

import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BookmarkResponseView {
  private int bookmarkSize;
  private List<BookmarkSimpleResponseView> bookmarks;

  public BookmarkResponseView(List<BookmarkSimpleResponseView> bookmarks) {
    bookmarkSize = bookmarks.size();
    this.bookmarks = bookmarks;
  }

  public int getBookmarkSize() {
    return bookmarkSize;
  }

  public List<BookmarkSimpleResponseView> getBookmarks() {
    return bookmarks;
  }

}
