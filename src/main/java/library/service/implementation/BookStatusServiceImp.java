package library.service.implementation;

import library.model.BookStatus;
import library.repository.BookStatusRepository;
import library.service.interfaces.BookStatusService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookStatusServiceImp implements BookStatusService {

    private final BookStatusRepository bookStatusRepository;

    public BookStatusServiceImp(BookStatusRepository bookStatusRepository) {
        this.bookStatusRepository = bookStatusRepository;
    }

    @Override
    public BookStatus getById(Long bookStatusId) {
        return bookStatusRepository.getById(bookStatusId);
    }

    @Override
    public List<BookStatus> getAll() {
        return bookStatusRepository.findAll();
    }
}
