package library.controller.books;

import library.model.Book;
import library.model.BookStatus;
import library.model.Chapter;
import library.service.interfaces.BookService;
import library.service.interfaces.BookStatusService;
import library.service.interfaces.ChapterService;
import library.service.interfaces.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/works/manage")
public class ManageBookController {

    private final BookService bookService;
    private final BookStatusService bookStatusService;
    private final ChapterService chapterService;
    private final UserService userService;

    public ManageBookController(BookService bookService, BookStatusService bookStatusService, ChapterService chapterService, UserService userService) {
        this.bookService = bookService;
        this.bookStatusService = bookStatusService;
        this.chapterService = chapterService;
        this.userService = userService;
    }

///********************! Управление поездками водителей !********************

    @GetMapping("/{bookId}")
    public String managementById(@PathVariable Long bookId, ModelMap model) {
        Book book = bookService.getById(bookId);
        model.addAttribute("book", book);
        model.addAttribute("reader", userService.getRemoteUser());
        List<BookStatus> bookStatusList = bookStatusService.getAll();
        model.addAttribute("statusList", bookStatusList);
        List<Chapter> chapters = chapterService.findByBookId(bookId);
        model.addAttribute("chapterList", chapters);
        String userName;
        if (userService.getRemoteUser() == null)
            userName = "NaN";
        else
            userName = userService.getRemoteUser().getUsername();
        model.addAttribute("userName", userName);
        return "book/bookManage";
    }

    @Transactional
    @PostMapping("/{bookId}/setStatus")
    @PreAuthorize("(@userServiceImp.getRemoteUser().getId() == @bookServiceImp.getById(#bookId).creator.getId())")
    public String setupStatus(@PathVariable Long bookId, @RequestParam Long statusSelectId) {
        Book book = bookService.getById(bookId);
        if (book != null && statusSelectId >= 0 && statusSelectId <= 3){
            book.setBookStatus(bookStatusService.getById(statusSelectId));
            bookService.saveBook(book);
        }
        return "redirect:/works/manage/" + bookId;
    }

    @Transactional
    @PostMapping("/{bookId}/setName")
    @PreAuthorize("(@userServiceImp.getRemoteUser().getId() == @bookServiceImp.getById(#bookId).creator.getId())")
    public String setupName(@PathVariable Long bookId, Book book, ModelMap model) {
        Book bok = bookService.getById(bookId);
        bok.setName(book.getName());
        bookService.saveBook(bok);
        return "redirect:/works/manage/" + bookId;
    }

    @Transactional
    @PostMapping("/{bookId}/setCost")
    @PreAuthorize("(@userServiceImp.getRemoteUser().getId() == @bookServiceImp.getById(#bookId).creator.getId())")
    public String setupCost(@PathVariable Long bookId, Book book, ModelMap model) {
        Book bok = bookService.getById(bookId);
        bok.setCost(book.getCost());
        bookService.saveBook(bok);
        return "redirect:/works/manage/" + bookId;
    }

    @Transactional
    @PostMapping("/{bookId}/setDescription")
    @PreAuthorize("(@userServiceImp.getRemoteUser().getId() == @bookServiceImp.getById(#bookId).creator.getId())")
    public String setupDescription(@PathVariable Long bookId, Book book, ModelMap model) {
        Book bok = bookService.getById(bookId);
        bok.setDescription(book.getDescription());
        bookService.saveBook(bok);
        return "redirect:/works/manage/" + bookId;
    }
}
