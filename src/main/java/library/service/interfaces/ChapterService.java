package library.service.interfaces;

import library.model.Chapter;

import java.util.List;

public interface ChapterService {
    Chapter save(Chapter chapter);

    Chapter getById(Long chapterId);

    List<Chapter> findByBookId(Long bookId);
}
