package library.controller.profile;

import library.model.User;
import library.service.interfaces.UserService;
import library.service.interfaces.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/profile")
@Controller
public class ProfilesController {
    private final UserService userService;
    private final WalletService walletService;

    @Autowired
    public ProfilesController(UserService userService, WalletService walletService) {
        this.userService = userService;
        this.walletService = walletService;
    }

    @GetMapping
    public String myProfile() {
        return "redirect:/profile/" + userService.getRemoteUserId();
    }

    @GetMapping("/{id}")
    @PreAuthorize("(@userServiceImp.getRemoteUser().getId() == #id) or hasAuthority('ADMINISTRATOR')")
    public String viewProfileById(@PathVariable Long id, ModelMap model) {
        User user = userService.getById(id);
        long walletBalance = walletService.getBalance(id);
        if (user == null)
            return myProfile();
        model.addAttribute("user", user);
        model.addAttribute("balance", walletBalance);
        return "profile/profile";
    }

    @GetMapping("/change_email")
    public String changeEmail(ModelMap model) {
        model.addAttribute("currentEmail", userService.getRemoteUserEmail());
        return "profile/change_email";
    }

    @PostMapping("/change_email")
    public String changeEmail(@RequestParam("email") String email, ModelMap model) {
        if (userService.emailExists(email)) {
            model.addAttribute("currentEmail", userService.getRemoteUserEmail());
            model.addAttribute("emailExistsError", "Email already exists");
            return "profile/change_email";
        }
        userService.saveEmail(email);
        return "redirect:/profile";
    }

    @GetMapping("/change_password")
    public String changePassword() {
        return "profile/change_password";
    }

    @GetMapping("/grant_role_author")
    public String grantRoleAuthor() {
        userService.grantRoleAuthor();
        return "redirect:/profile";
    }

    @PostMapping("/change_password")
    public String changePassword(
            @RequestParam("newPassword") String newPassword
            , @RequestParam("currentPassword") String currentPassword
            , @RequestParam("confirmPassword") String confirmPassword, ModelMap model
    ) {
        if (!userService.checkRemoteUserPassword(currentPassword)) {
            model.addAttribute("InvalidPasswordError", "Invalid password");
            return "profile/change_password";
        }
        if (newPassword.isBlank()) {
            model.addAttribute("passwordIsBlankError", "New password must be not null!");
            return "profile/change_password";
        }
        if (!confirmPassword.equals(newPassword)) {
            model.addAttribute("currentPassword", currentPassword);
            model.addAttribute("passwordIsDifferentError", "Passwords are different");
            return "profile/change_password";
        }
        userService.savePassword(newPassword);
        return "redirect:/profile";
    }
}
