package library.controller.books;

import library.controller.paging.AbstractPrimaryPagingController;
import library.model.Book;
import library.service.implementation.books.BookSearchRequest;
import library.service.interfaces.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/works/search")
@Controller
public class BookSearchController extends AbstractPrimaryPagingController{

    private final BookService bookService;

    public BookSearchController(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    protected Long pageCount() {
        return bookService.pageCountByCreatorId();
    }

    @Override
    protected String getPrefix() {
        return "/works/search";
    }

    @GetMapping
    public String searchWorks(){
        return "redirect:/works/search/list/1";
    }

    @GetMapping("/list/{currentPage}")
    public String searchWorks(@PathVariable Long currentPage, BookSearchRequest bookSearchRequest, ModelMap model){
        if (currentPage < 1L)
            return firstPage();
        Long nPage = bookService.pageCountByBookSearch(bookSearchRequest);
        if (currentPage > nPage)
            return lastPage();
        model.addAttribute("nPage", nPage);
        model.addAttribute("currentPage", currentPage);
        List<Book> bookList = bookService.searchedBookListByNumberPageListAndBookSearchRequest(
                currentPage, bookSearchRequest);
        model.addAttribute("search", bookSearchRequest);
        model.addAttribute("books", bookList);
        return "book/view_searched_books";
    }

}
