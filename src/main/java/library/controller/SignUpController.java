package library.controller;

import library.model.User;
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
        int errs = 0;
        if (userService.getByUsername(user.getUsername()) != null) {
            model.addAttribute("usernameExistsError", "Username already exists");
            errs++;
        }
        if (user.getPassword().isBlank() || !user.getPassword().matches("^[A-Za-z0-9#$&/%-._]{8,60}$")) {
            model.addAttribute("passwordIsInvalid", "Password must have only [A-Za-z0-9#$&/%-._] 8 to 60 chars!");
            errs++;
        }
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("passwordsAreDifferent", "Passwords are different");
            errs++;
        }
        if (userService.emailExists(user.getEmail())) {
            model.addAttribute("emailExistsError", "Email already exists");
            errs++;
        }
        if (errs > 0) {
            return "sign_up";
        }
        Long[] rolesId = new Long[1];
        rolesId[0] = roleService.findByName("READER").getId();
        userService.signUp(user, roleService.getRoleSetByIds(rolesId));
        return "redirect:/sign_in";
    }
}
