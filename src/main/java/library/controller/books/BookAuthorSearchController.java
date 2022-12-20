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

@RequestMapping("/author")
@Controller
public class BookAuthorSearchController extends AbstractPrimaryPagingController{

    private final UserService userService;
    private final BookService bookService;

    public BookAuthorSearchController(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }

    @Override
    protected Long pageCount() {
        return bookService.pageCountByCreatorId();
    }

    @Override
    protected String getPrefix() {
        return "/author/works";
    }

    @GetMapping("/works/list")
    public String myWorks(BookSearchRequest bookSearchRequest, ModelMap model){
        return myWorks(1L, bookSearchRequest, model);
    }

    @GetMapping("/works/list/{currentPage}")
    public String myWorks(@PathVariable Long currentPage, BookSearchRequest bookSearchRequest, ModelMap model){
        if (currentPage < 1L)
            return firstPage();
        Long nPage = bookService.pageCountByBookSearch(bookSearchRequest);
        if (currentPage > nPage)
            return lastPage();
        model.addAttribute("nPage", nPage);
        model.addAttribute("currentPage", currentPage);
        bookSearchRequest.setCreatorName(userService.getRemoteUser().getUsername());
        List<Book> bookList = bookService.searchedBookListByNumberPageListAndBookSearchRequest(
                currentPage, bookSearchRequest);
        model.addAttribute("search", bookSearchRequest);
        model.addAttribute("books", bookList);
        return "book/view_searched_books";
    }


}
