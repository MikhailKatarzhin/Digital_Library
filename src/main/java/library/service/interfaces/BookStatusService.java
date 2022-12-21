package library.service.interfaces;

import library.model.BookStatus;

import java.util.List;

public interface BookStatusService {
    BookStatus getById(Long bookStatusId);

    List<BookStatus> getAll();
}
