package imwhs.krit_server_sandbox.controller;

import imwhs.krit_server_sandbox.domain.Account;
import imwhs.krit_server_sandbox.dto.account.*;
import imwhs.krit_server_sandbox.security.annotation.AuthenticatedKritAccountId;
import imwhs.krit_server_sandbox.service.AccountService;
import jakarta.validation.Valid;
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
    public AccountResponse registerAccount(@Valid @RequestBody AccountRegisterRequest request) {
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
    @PutMapping("/{id}/password") // TODO: PATCH?
    public void updateAccountPassword(
            @PathVariable Long id,
            @Valid @RequestBody AccountPasswordUpdateRequest request,
            @AuthenticatedKritAccountId Long actorId) {
        accountService.updatePassword(actorId, id, request.password());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}/name") // TODO: PATCH?
    public void updateAccountName(
            @PathVariable Long id,
            @Valid @RequestBody AccountNameUpdateRequest request,
            @AuthenticatedKritAccountId Long actorId) {
        accountService.updateName(actorId, id, request.name());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}/imageUrl") // TODO: PATCH?
    public void updateAccountImageUrl(
            @PathVariable Long id,
            @Valid @RequestBody AccountImageUrlUpdateRequest request,
            @AuthenticatedKritAccountId Long actorId) {
        accountService.updateImageUrl(actorId, id, request.imageUrl());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void softDeleteAccount(
            @PathVariable Long id,
            @AuthenticatedKritAccountId Long actorId) {
        accountService.softDelete(actorId, id);
    }

}
