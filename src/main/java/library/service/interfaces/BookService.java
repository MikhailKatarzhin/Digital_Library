package library.service.interfaces;

import library.model.Book;
import library.service.implementation.books.BookSearchRequest;

import java.util.List;

public interface BookService {

    Book saveBook(Book book);

    Book saveNewBook(Book book);

    Book getById(long id);

    long pageCount();

    long pageCountByCreatorId();

    long pageCountByBookSearch(BookSearchRequest bookSearchRequest);

    long pageCountByBookSearchRemoteReader(BookSearchRequest bookSearchRequest);

    List<Book> searchedBookListByNumberPageListAndBookSearchRequest(long currentPage, BookSearchRequest bookSearchRequest);

    List<Book> searchedBookListByNumberPageListAndBookSearchRequestRemoteReader(long currentPage, BookSearchRequest bookSearchRequest);

    List<Book> searchedBookListByNumberPageListAndBookSearchRequestRemoteAuthor(long currentPage, BookSearchRequest bookSearchRequest);
}
