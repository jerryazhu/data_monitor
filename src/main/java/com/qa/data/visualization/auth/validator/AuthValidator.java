package com.qa.data.visualization.auth.validator;

import com.qa.data.visualization.auth.entities.User;
import com.qa.data.visualization.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AuthValidator implements Validator {
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        Object value = errors.getFieldValue("username");
        if (value == null || !StringUtils.hasText(value.toString())) {
            errors.rejectValue("username", "NotEmpty");
            return;
        }
        if (user.getUsername().length() < 4 || user.getUsername().length() > 32) {
            errors.rejectValue("username", "Size.userForm.username");
            return;
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.userForm.username");
            return;
        }

        Object value2 = errors.getFieldValue("password");
        if (value2 == null || !StringUtils.hasText(value2.toString())) {
            errors.rejectValue("password", "NotEmpty");
            return;
        }
        if (user.getPassword().length() < 4 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password");
            return;
        }

        if (!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
        }
    }
}