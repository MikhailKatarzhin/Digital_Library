package library.controller.books;

import library.controller.paging.AbstractPrimaryPagingController;
import library.model.Book;
import library.service.implementation.books.BookSearchRequest;
import library.service.interfaces.BookService;
import library.service.interfaces.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/reader/library/search")
@Controller
public class BookReaderSearchController extends AbstractPrimaryPagingController{

    private final UserService userService;
    private final BookService bookService;

    public BookReaderSearchController(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }

    @Override
    protected Long pageCount() {
        return bookService.pageCount();
    }

    @Override
    protected String getPrefix() {
        return "/reader/library/search";
    }

    @GetMapping
    public String myWorks(){
        return "redirect:/reader/library/search/list/1";
    }

    @GetMapping("/list/{currentPage}")
    public String myWorks(@PathVariable Long currentPage, BookSearchRequest bookSearchRequest, ModelMap model){
        bookSearchRequest.setCreatorName(userService.getRemoteUser().getUsername());
        if (currentPage < 1L)
            return firstPage();
        Long nPage = bookService.pageCountByBookSearchRemoteReader(bookSearchRequest);
        if (currentPage > nPage)
            return lastPage();
        model.addAttribute("nPage", nPage);
        model.addAttribute("currentPage", currentPage);
        List<Book> bookList = bookService.searchedBookListByNumberPageListAndBookSearchRequestRemoteReader(
                currentPage, bookSearchRequest);
        model.addAttribute("search", bookSearchRequest);
        model.addAttribute("books", bookList);
        return "book/reader_view_searched_books";
    }
}
