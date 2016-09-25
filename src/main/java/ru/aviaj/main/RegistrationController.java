package ru.aviaj.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import ru.aviaj.service.AccountService;

/**
 * Created by sibirsky on 25.09.16.
 */

@RestController
public class RegistrationController {

    private final AccountService accountService;

    @Autowired
    public RegistrationController(AccountService accountService) {
        this.accountService = accountService;
    }
}
