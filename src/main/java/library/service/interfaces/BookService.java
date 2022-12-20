package library.service.interfaces;

import library.model.Book;
import library.service.implementation.books.BookSearchRequest;

import java.util.List;

public interface BookService {

    Book saveBook(Book book);

    Book getById(long id);

    long pageCountByCreatorId();

    long pageCountByBookSearch(BookSearchRequest bookSearchRequest);

    List<Book> searchedBookListByNumberPageListAndBookSearchRequest(long currentPage, BookSearchRequest bookSearchRequest);
}