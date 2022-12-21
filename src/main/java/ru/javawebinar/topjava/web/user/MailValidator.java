package ru.javawebinar.topjava.web.user;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.HasId;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.web.ExceptionInfoHandler;

@Component
public class MailValidator implements Validator {
    private final UserRepository repository;

    public MailValidator(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return HasId.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String email = null;
        Integer id = null;
        if (target instanceof UserTo) {
            UserTo userTo = ((UserTo) target);
            email = userTo.getEmail();
            id = userTo.getId();
        } else if (target instanceof User) {
            User user = ((User) target);
            email = user.getEmail();
            id = user.getId();
        }
        if (email != null && !"".equals(email)) {
            User foundUser = repository.getByEmail(email.toLowerCase());
            if (foundUser != null && !foundUser.getId().equals(id)) {
                errors.rejectValue("email", ExceptionInfoHandler.EXCEPTION_DUPLICATE_EMAIL);
            }
        }
    }
}
