package library.service.implementation;

import library.model.BookStatus;
import library.repository.BookStatusRepository;
import library.service.interfaces.BookStatusService;
import org.springframework.stereotype.Service;

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
}
