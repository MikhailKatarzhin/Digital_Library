package library.controller.books;

import library.model.Book;
import library.service.interfaces.BookService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/author/works")
@Controller
public class BookAuthorAddController{

    private final BookService bookService;

    public BookAuthorAddController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/add")
    @PreAuthorize("@userServiceImp.getRemoteUser().hasRole('AUTHOR')")
    public String addWork(ModelMap model){
        model.addAttribute("book", new Book());
        return "book/addBook";
    }

    @PostMapping("/add")
    @PreAuthorize("@userServiceImp.getRemoteUser().hasRole('AUTHOR')")
    public String addWork(Book book, ModelMap model){
        int errs = 0;
        if (book.getName() == null || book.getName() .isBlank() || book.getName() .isEmpty()
                || !book.getName().matches("^[A-Za-z0-9 А-Яа-яЁё.,\"!?:;-]{1,200}$")) {
            model.addAttribute("bookNameInvalid", "Book name is Invalid");
            errs++;
        }
        String description = book.getDescription();
        if (description == null
                || description.length() > 2048) {
            model.addAttribute("bookDescriptionInvalid", "Book description is Invalid");
            errs++;
        }
        if (description!=null)
            if (description.isBlank()) {
                description = "";
                book.setDescription(description);
            }
        if (book.getCost() < 0 || book.getCost() > 10000) {
            model.addAttribute("bookCostInvalid", "Book must be in [0 <= cost <= 10 000]");
            errs++;
        }
        if (book.getYearOfCreation() < -10000 || book.getYearOfCreation() > 2100) {
            model.addAttribute("bookYearInvalid", "Book must be in [-10 000 <= year creation <= 2100]");
            errs++;
        }
        if (errs > 0) {
            return "book/addBook";
        }
        bookService.saveBook(book);
        return "redirect:/author/works/search";
    }
}
