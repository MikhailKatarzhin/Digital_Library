package library.service.interfaces;

import library.model.BookStatus;

public interface BookStatusService {
    BookStatus getById(Long bookStatusId);
}
