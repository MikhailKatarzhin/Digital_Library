package library.controller.books;

import library.model.Book;
import library.model.User;
import library.service.implementation.books.BookSearchRequest;
import library.service.interfaces.BookService;
import library.service.interfaces.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;

@RequestMapping("/reader/buy")
@Controller
public class BookBuyingController {

    private final BookService bookService;
    private final UserService userService;

    public BookBuyingController(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping("/{bookId}")
    public String buyBookView(@PathVariable Long bookId, ModelMap model) {
        model.addAttribute("bookId", bookId);
        model.addAttribute("bookCost", bookService.getById(bookId).getCost());
        model.addAttribute("bookName", bookService.getById(bookId).getName());
        model.addAttribute("walletBalance", userService.getRemoteUser().getWallet().getBalance());
        return "book/buyBook";
    }

    @Transactional
    @PostMapping("/{bookId}")
    public String buyBook(@PathVariable Long bookId, ModelMap model){
        Book book = bookService.getById(bookId);
        if (book == null)
            return "redirect:/works/search";
        if (userService.getRemoteUser().hasBook(bookId))
            return "redirect:/works/manage/"+bookId;
        long errs = 0;
        if (userService.getRemoteUser().getWallet().getBalance() < book.getCost()){
            errs++;
            model.addAttribute("HaveNotMoneyError", "Your balance of wallet less than bok cost");
        }
        userService.addPurchasedBook(book);
        if (errs > 0 ) {
            model.addAttribute("bookId", bookId);
            model.addAttribute("bookCost", bookService.getById(bookId).getCost());
            model.addAttribute("bookName", bookService.getById(bookId).getName());
            model.addAttribute("walletBalance", userService.getRemoteUser().getWallet().getBalance());
            return "book/buyBook";
        }
        return "redirect:/works/manage/"+bookId;
    }
}
