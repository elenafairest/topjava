package ru.javawebinar.topjava.web.user;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.HasId;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.web.ExceptionInfoHandler;

import java.lang.reflect.Method;

@Component
public class MailValidator implements Validator {
    private final UserRepository repository;

    public MailValidator(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        if (!HasId.class.isAssignableFrom(clazz)) {
            return false;
        }
        for (Method m : clazz.getMethods()) {
            if ("getEmail".equals(m.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        String email = null;
        Integer id = null;
        if (target instanceof UserTo userTo) {
            email = userTo.getEmail();
            id = userTo.getId();
        } else if (target instanceof User user) {
            email = user.getEmail();
            id = user.getId();
        }
        if (StringUtils.hasLength(email)) {
            User foundUser = repository.getByEmail(email.toLowerCase());
            if (foundUser != null && !foundUser.getId().equals(id)) {
                errors.rejectValue("email", ExceptionInfoHandler.EXCEPTION_DUPLICATE_EMAIL);
            }
        }
    }
}
