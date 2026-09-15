package imwhs.krit_server_sandbox.controller;

import imwhs.krit_server_sandbox.domain.Account;
import imwhs.krit_server_sandbox.dto.AccountRegisterRequest;
import imwhs.krit_server_sandbox.dto.AccountResponse;
import imwhs.krit_server_sandbox.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public AccountResponse getAccount(@PathVariable Long id) {
        Account account = accountService.get(id);
        return AccountResponse.from(account);
    }

    @GetMapping
    public AccountResponse getAccountByHandle(@RequestParam String handle) {
        Account account = accountService.getByHandle(handle);
        return AccountResponse.from(account);
    }

}
