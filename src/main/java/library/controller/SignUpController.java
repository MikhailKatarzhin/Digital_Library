package library.controller;

import library.model.User;
import library.model.Wallet;
import library.service.interfaces.RoleOfUserService;
import library.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/sign_up")
@Controller
public class SignUpController {

    private final UserService userService;
    private final RoleOfUserService roleService;

    @Autowired
    public SignUpController(UserService userService, RoleOfUserService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String signUp(ModelMap model) {
        if (userService.getRemoteUser() != null)
            return "redirect:/profile";
        model.addAttribute("user", new User());
        return "sign_up";
    }

    @PostMapping
    public String addUser(@RequestParam String confirmPassword, User user, ModelMap model) {
        if (userService.getByUsername(user.getUsername()) != null) {
            model.addAttribute("usernameExistsError", "Username already exists");
        }
        if (user.getPassword().isBlank()) {
            model.addAttribute("passwordIsBlankError", "Password must be not blank!");
        }
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("passwordsAreDifferent", "Passwords are different");
        }
        if (userService.emailExists(user.getEmail())) {
            model.addAttribute("emailExistsError", "Email already exists");
        }
        if (model.size() > 2) {
            return "sign_up";
        }
        Long[] rolesId = new Long[1];
        rolesId[0] = roleService.findByName("READER").getId();
        userService.signUp(user, roleService.getRoleSetByIds(rolesId));
        return "redirect:/sign_in";
    }
}
