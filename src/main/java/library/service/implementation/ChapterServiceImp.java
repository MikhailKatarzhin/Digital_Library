package library.service.implementation;

import library.model.Chapter;
import library.repository.ChapterRepository;
import library.service.interfaces.ChapterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChapterServiceImp implements ChapterService {

    private final ChapterRepository chapterRepository;

    public ChapterServiceImp(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    public Chapter save(Chapter chapter){
        return chapterRepository.save(chapter);
    }

    public Chapter getById(Long chapterId){
        return chapterRepository.getById(chapterId);
    }

    @Override
    public List<Chapter> findByBookId(Long bookId) {
        return chapterRepository.findByBookId(bookId);
    }


}
