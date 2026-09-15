package imwhs.krit_server_sandbox.controller;

import imwhs.krit_server_sandbox.domain.Account;
import imwhs.krit_server_sandbox.dto.*;
import imwhs.krit_server_sandbox.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v0/accounts")
@RequiredArgsConstructor
@RestController
public class AccountController {

    private final AccountService accountService;

    @ResponseStatus(HttpStatus.CREATED)
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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}/password")
    public void updateAccountPassword(
            @PathVariable Long id,
            @RequestBody AccountPasswordUpdateRequest request) {
        accountService.updatePassword(id, request.password());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}/name")
    public void updateAccountName(
            @PathVariable Long id,
            @RequestBody AccountNameUpdateRequest request) {
        accountService.updateName(id, request.name());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}/imageUrl")
    public void updateAccountImageUrl(
            @PathVariable Long id,
            @RequestBody AccountImageUrlUpdateRequest request) {
        accountService.updateImageUrl(id, request.imageUrl());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void softDeleteAccount(
            @PathVariable Long id,
            @RequestParam Long actorId) { // TODO: 스프링 시큐리티 도입 후 세션 인증 정보에서 가져오도록 변경
        accountService.softDelete(id, actorId);
    }

}
