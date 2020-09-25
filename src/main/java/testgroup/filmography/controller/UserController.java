package testgroup.filmography.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import testgroup.filmography.dto.UserDto;
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

    @RequestMapping(value = "admin/create", method = RequestMethod.GET)
    public String showCreateUserForm(@ModelAttribute("userDto") UserDto userDto) {
        return "create";
    }

    @RequestMapping(value = "admin/create", method = RequestMethod.POST)
    public String createUser(@ModelAttribute("useDto") UserDto userDto) {
        User user = new User();
        user.setName(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setRoles(getRoles(userDto.getRoles()));
        userService.createUser(user);
        return "redirect:/admin/show";
    }

    @RequestMapping(value = "admin/show", method = RequestMethod.GET)
    public String adminPage(ModelMap model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "show";
    }

    @RequestMapping(value = "admin/update", method = RequestMethod.GET)
    public String showUpdateUserForm(@ModelAttribute("userDto") UserDto userDto,
                                     @RequestParam("id") long id) {
        userDto.setId(id);
        return "update";
    }

    @RequestMapping(value = "admin/update", method = RequestMethod.POST)
    public String updateUser(@ModelAttribute("userDto") UserDto userDto) {
        User user = userService.getUserById(userDto.getId());
        Set<Role> roles = user.getRoles();
        user.setName(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setRoles(getRoles(userDto.getRoles()));
        userService.updateUser(user);
        roles.forEach(x -> roleService.deleteRole(x.getId()));
        return "redirect:/admin/show";
    }

    @RequestMapping(value = "admin/delete", method = RequestMethod.GET)
    public String deleteUser(@RequestParam("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin/show";
    }

    public Set<Role> getRoles(List<String> rolesForUser) {
        Set<Role> roles = new HashSet<>();
        if (rolesForUser.contains("admin")) {
            roles.add(new Role("ROLE_ADMIN"));
        }
        if (rolesForUser.contains("user")) {
            roles.add(new Role("ROLE_USER"));
        }
        return roles;
    }
}
