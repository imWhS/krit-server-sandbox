package imwhs.krit_server_sandbox.controller;

import imwhs.krit_server_sandbox.domain.Account;
import imwhs.krit_server_sandbox.dto.AccountRegisterRequest;
import imwhs.krit_server_sandbox.dto.AccountResponse;
import imwhs.krit_server_sandbox.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v0/account")
@RequiredArgsConstructor
@RestController
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public AccountResponse registerAccount(@RequestBody AccountRegisterRequest request) {
        Account account = accountService.register(request.getHandle(), request.getEmail(), request.getPassword());
        return AccountResponse.from(account);
    }



}
