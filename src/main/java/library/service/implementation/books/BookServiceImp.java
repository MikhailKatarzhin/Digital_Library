package library.service.implementation.books;

import library.model.Book;
import library.repository.BookRepository;
import library.service.interfaces.BookService;
import library.service.interfaces.BookStatusService;
import library.service.interfaces.UserService;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

import static library.config.ProjectConstants.ROW_COUNT;

@Service
public class BookServiceImp implements BookService {

    private final BookRepository bookRepository;
    private final BookStatusService bookStatusService;
    private final UserService userService;

    BookServiceImp(BookRepository bookRepository, BookStatusService bookStatusService, UserService userService){
        this.bookRepository = bookRepository;
        this.bookStatusService = bookStatusService;
        this.userService = userService;
    }
    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book saveNewBook(Book book) {
        book.setYearOfUpload((short) Year.now().getValue());
        book.setCreator(userService.getRemoteUser());
        book.setBookStatus(bookStatusService.getById(1L));
        return bookRepository.save(book);
    }

    @Override
    public Book getById(long id) {
        return bookRepository.getById(id);
    }

    @Override
    public long pageCount() {
        long nSearchedBook = bookRepository.count();
        long nPage = nSearchedBook / ROW_COUNT + (nSearchedBook % ROW_COUNT == 0 ? 0 : 1);
        return nPage == 0 ? nPage + 1 : nPage;
    }

    public long pageCountByCreatorId() {
        long nSearchedBook = bookRepository.countByCreatorId(userService.getRemoteUserId());
        long nPage = nSearchedBook / ROW_COUNT + (nSearchedBook % ROW_COUNT == 0 ? 0 : 1);
        return nPage == 0 ? nPage + 1 : nPage;
    }

    @Override
    public long pageCountByBookSearch(BookSearchRequest bookSearchRequest) {
        Long nSearchedBook = bookRepository.countBookByBookSearchRequest(
                bookSearchRequest.getBookStatusName(), "%" + bookSearchRequest.getCreatorName() + "%",
                "%" + bookSearchRequest.getBookName() + "%", bookSearchRequest.getMinCost(),
                bookSearchRequest.getMaxCost(), bookSearchRequest.getMinYoU(),
                bookSearchRequest.getMaxYoU(), bookSearchRequest.getMinYoC(),
                bookSearchRequest.getMaxYoC()
        );
        long nPage = nSearchedBook / ROW_COUNT + (nSearchedBook % ROW_COUNT == 0 ? 0 : 1);
        return nPage == 0 ? nPage + 1 : nPage;
    }

    @Override
    public long pageCountAvailableByBookSearch(BookSearchRequest bookSearchRequest) {
        Long nSearchedBook = bookRepository.countNotHiddenBookByBookSearchRequest(
                bookSearchRequest.getBookStatusName(), "%" + bookSearchRequest.getCreatorName() + "%",
                "%" + bookSearchRequest.getBookName() + "%", bookSearchRequest.getMinCost(),
                bookSearchRequest.getMaxCost(), bookSearchRequest.getMinYoU(),
                bookSearchRequest.getMaxYoU(), bookSearchRequest.getMinYoC(),
                bookSearchRequest.getMaxYoC()
        );
        long nPage = nSearchedBook / ROW_COUNT + (nSearchedBook % ROW_COUNT == 0 ? 0 : 1);
        return nPage == 0 ? nPage + 1 : nPage;
    }

    @Override
    public long pageCountByBookSearchRemoteReader(BookSearchRequest bookSearchRequest) {
        Long nSearchedBook = bookRepository.countBookByBookSearchRequestAndReaderId(
                bookSearchRequest.getBookStatusName(), "%" + bookSearchRequest.getCreatorName() + "%",
                "%" + bookSearchRequest.getBookName() + "%", bookSearchRequest.getMinCost(),
                bookSearchRequest.getMaxCost(), bookSearchRequest.getMinYoU(),
                bookSearchRequest.getMaxYoU(), bookSearchRequest.getMinYoC(),
                bookSearchRequest.getMaxYoC(), userService.getRemoteUserId()
        );
        long nPage = nSearchedBook / ROW_COUNT + (nSearchedBook % ROW_COUNT == 0 ? 0 : 1);
        return nPage == 0 ? nPage + 1 : nPage;
    }

    @Override
    public List<Book> searchedBookListByNumberPageListAndBookSearchRequest(long currentPage, BookSearchRequest bookSearchRequest) {
        return bookRepository.getBookListByBookSearchRequest(
                bookSearchRequest.getBookStatusName(), "%" + bookSearchRequest.getCreatorName() + "%",
                "%" + bookSearchRequest.getBookName() + "%", bookSearchRequest.getMinCost(),
                bookSearchRequest.getMaxCost(), bookSearchRequest.getMinYoU(),
                bookSearchRequest.getMaxYoU(), bookSearchRequest.getMinYoC(),
                bookSearchRequest.getMaxYoC(), ROW_COUNT, (currentPage - 1) * ROW_COUNT
        );
    }

    @Override
    public List<Book> searchedAvailableBookListByNumberPageListAndBookSearchRequest(long currentPage, BookSearchRequest bookSearchRequest) {
        return bookRepository.getNotHiddenBookListByBookSearchRequest(
                bookSearchRequest.getBookStatusName(), "%" + bookSearchRequest.getCreatorName() + "%",
                "%" + bookSearchRequest.getBookName() + "%", bookSearchRequest.getMinCost(),
                bookSearchRequest.getMaxCost(), bookSearchRequest.getMinYoU(),
                bookSearchRequest.getMaxYoU(), bookSearchRequest.getMinYoC(),
                bookSearchRequest.getMaxYoC(), ROW_COUNT, (currentPage - 1) * ROW_COUNT
        );
    }

    @Override
    public List<Book> searchedBookListByNumberPageListAndBookSearchRequestRemoteReader(long currentPage, BookSearchRequest bookSearchRequest) {
        return bookRepository.getBookListByBookSearchRequestAndReaderId(
                bookSearchRequest.getBookStatusName(), "%" + bookSearchRequest.getCreatorName() + "%",
                "%" + bookSearchRequest.getBookName() + "%", bookSearchRequest.getMinCost(),
                bookSearchRequest.getMaxCost(), bookSearchRequest.getMinYoU(),
                bookSearchRequest.getMaxYoU(), bookSearchRequest.getMinYoC(),
                bookSearchRequest.getMaxYoC(),  userService.getRemoteUserId(),
                ROW_COUNT, (currentPage - 1) * ROW_COUNT
        );
    }

    @Override
    public List<Book> searchedBookListByNumberPageListAndBookSearchRequestRemoteAuthor(long currentPage, BookSearchRequest bookSearchRequest) {
        bookSearchRequest.setCreatorName(userService.getRemoteUser().getUsername());
        return searchedBookListByNumberPageListAndBookSearchRequest(currentPage, bookSearchRequest);
    }
}
