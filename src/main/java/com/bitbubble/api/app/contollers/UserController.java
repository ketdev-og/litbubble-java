package com.bitbubble.api.app.contollers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bitbubble.api.app.entitiy.Role;
import com.bitbubble.api.app.entitiy.User;
import com.bitbubble.api.app.service.RoleService;
import com.bitbubble.api.app.service.UserService;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @PostMapping({ "/register_user" })
    private User createUser(@RequestBody User user) {
        Set<Role> userRole = roleService.findByRoleName("Admin");
        user.setRole(userRole);

        return userService.createUser(user);
    }

    @GetMapping({"/admin_url"})
    public String forAdmin() {
        return "Admin url";
    }

    @GetMapping({"/user_url"})
    public String forUser() {
        return "User url";
    }

}
