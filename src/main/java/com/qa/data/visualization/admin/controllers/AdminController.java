package com.qa.data.visualization.admin.controllers;


import com.qa.data.visualization.auth.entities.Role;
import com.qa.data.visualization.auth.entities.Role2;
import com.qa.data.visualization.auth.entities.User;
import com.qa.data.visualization.auth.repositories.Role2Repository;
import com.qa.data.visualization.auth.repositories.RoleRepository;
import com.qa.data.visualization.auth.repositories.UserRepository;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;
import com.web.spring.datatable.DatatablesResponse;
import com.web.spring.datatable.TableQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private UserRepository userRepository;
    @PersistenceContext(unitName = "primaryPersistenceUnit")
    private EntityManager entityManager;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private Role2Repository role2Repository;
    @Autowired
    private RoleRepository roleRepository;

    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        model.addAttribute("username", name);
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            model.addAttribute(grantedAuthority.getAuthority(), true);
        }
        return "admin";
    }

    @RequestMapping("/view/{viewName}")
    public String view(@PathVariable String viewName, Model model, HttpServletRequest request) {
        if (viewName.contains("---")) {
            viewName = viewName.replaceAll("---", "/");
        }
        List<Role2> roleList = role2Repository.findAll();
        model.addAttribute("roleList", roleList);
        return viewName;
    }

    @MessageMapping("/notice")
    @SendTo("/topic/notice")
    public String notice(String notice) {
        logger.info("send notice:" + notice);
        return notice;
    }

    @RequestMapping("/get_all_users")
    @ResponseBody
    public DatatablesResponse<User> get_all_users(Model model, HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        TableQuery query = new TableQuery(entityManager, User.class, criterias);
        DataSet<User> users = query.getResultDataSet();
        return DatatablesResponse.build(users, criterias);
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public String resetPassword(@RequestParam(value = "id", required = true) Long id, @RequestParam(value = "password", required = true) String password) {
        User user = userRepository.findOne(id);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        return bCryptPasswordEncoder.encode(password);
    }

    @RequestMapping(value = "/updateAuthority", method = RequestMethod.POST)
    @ResponseBody
    public boolean updateAuthority(@RequestParam(value = "id", required = true) Long id, @RequestBody List<String> roleList) {
        User user = userRepository.findOne(id);
        Set<Role> roles = new HashSet<Role>();
        for (String roleName : roleList) {
            roles.add(roleRepository.findByName(roleName));
        }
        user.setRoles(roles);
        userRepository.save(user);
        return true;
    }

    @RequestMapping(value = "/newUser", method = RequestMethod.POST)
    @ResponseBody
    public boolean newUser(@RequestParam(value = "username", required = true) String username, @RequestParam(value = "password", required = true) String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setRoles(new HashSet<Role>() {{
            add(roleRepository.findByName("ROLE_USER"));
        }});
        userRepository.save(user);
        return true;
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    @ResponseBody
    public boolean deleteUser(@RequestParam(value = "id", required = true) Long id) {
        userRepository.delete(id);
        return true;
    }
}
