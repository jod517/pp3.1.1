package testgroup.filmography.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import testgroup.filmography.model.Role;
import testgroup.filmography.model.User;
import testgroup.filmography.service.RoleService;
import testgroup.filmography.service.UserService;
import testgroup.filmography.service.UserServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public String userPage(Authentication authentication, ModelMap model) {
        User user = userService.getUserByName(authentication.getName());
        model.addAttribute("user", user);
        return "user";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }

    @RequestMapping(value = "/admin/create", method = RequestMethod.GET)
    public String createUser(ModelMap model) {
        model.addAttribute("newUser", new User());
        return "create";
    }

    @RequestMapping(value = "/admin/create", method = RequestMethod.POST)
    public String createdUser(@ModelAttribute("newUser") User user,
                              @RequestParam(value = "adminRole", defaultValue = "") String adminRole,
                              @RequestParam(value = "userRole", defaultValue = "") String userRole) {

        user.setRoles(getRoles(adminRole, userRole));
        userService.createUser(user);
        return "redirect:/admin/show";
    }

    @RequestMapping(value = "/admin/show", method = RequestMethod.GET)
    public String adminPage(ModelMap model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "show";
    }

    @RequestMapping(value = "/admin/update", method = RequestMethod.GET)
    public String updateUser(@RequestParam(name = "id", defaultValue = "0") long id,
                             ModelMap model) {

        model.addAttribute("user", userService.getUserById(id));
        return "update";
    }

    @RequestMapping(value = "/admin/update", method = RequestMethod.POST)
    public String updatedUser(@ModelAttribute("user") User user,
                              @RequestParam(value = "adminRole", defaultValue = "") String adminRole,
                              @RequestParam(value = "userRole", defaultValue = "") String userRole) {

        Set<Role> roles = userService.getUserById(user.getId()).getRoles();
        user.setRoles(getRoles(adminRole, userRole));
        userService.updateUser(user);
        roles.forEach(x -> roleService.deleteRole(x.getId()));
        return "redirect:/admin/show";
    }

    @RequestMapping(value = "/admin/delete", method = RequestMethod.GET)
    public String deleteUser(@RequestParam(name = "id", defaultValue = "0") long id) {
        userService.deleteUser(id);
        return "redirect:/admin/show";
    }

    public Set<Role> getRoles(String adminRole, String userRole) {
        Set<Role> roles = new HashSet<>();
        if (!adminRole.isEmpty()) {
            roles.add(new Role(adminRole));
        }
        if (!userRole.isEmpty()) {
            roles.add(new Role(userRole));
        }
        return roles;
    }
}

