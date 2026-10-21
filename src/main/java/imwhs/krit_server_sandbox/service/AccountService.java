package imwhs.krit_server_sandbox.service;

import imwhs.krit_server_sandbox.domain.Account;
import imwhs.krit_server_sandbox.exception.account.AccountDuplicateEmailException;
import imwhs.krit_server_sandbox.exception.account.AccountDuplicateHandleException;
import imwhs.krit_server_sandbox.exception.account.AccountNotFoundException;
import imwhs.krit_server_sandbox.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Account register(String handle, String email, String password) {
        boolean isExistByHandle = accountRepository.existsByHandleAndDeletedAtIsNull(handle);
        if (isExistByHandle) {
            throw new AccountDuplicateHandleException(handle);
        }

        boolean isExistByEmail = accountRepository.existsByEmailAndDeletedAtIsNull(email);
        if (isExistByEmail) {
            throw new AccountDuplicateEmailException(email);
        }

        String encodedPassword = passwordEncoder.encode(password);
        Account account = Account.create(handle, email, encodedPassword);
        accountRepository.save(account);
        return account;
    }

    public Account get(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("계정의 ID가 유효하지 않아요.");
        }

        Account account = accountRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() ->
                new AccountNotFoundException(id));

        return account;
    }

    public Account getByHandle(String handle) {
        if (handle == null || handle.isBlank()) {
            throw new IllegalArgumentException("계정의 공개 식별자(핸들)가 유효하지 않아요.");
        }

        Account account = accountRepository.findByHandleAndDeletedAtIsNull(handle).orElseThrow(() ->
                new AccountNotFoundException("공개 식별자(핸들)가 " + handle + "인 해당하는 계정을 찾을 수 없어요."));

        return account;
    }

    @Transactional
    public void updatePassword(Long id, String password) {
        Account account = get(id);
        String encodedPassword = passwordEncoder.encode(password);
        account.updateEncodedPassword(encodedPassword);
    }

    @Transactional
    public void updateName(Long id, String name) {
        Account account = get(id);
        account.updateName(name);
    }

    @Transactional
    public void updateImageUrl(Long id, String imageUrl) {
        Account account = get(id);
        account.updateImageUrl(imageUrl);
    }

    @Transactional
    public void softDelete(Long id, Long actorId) {
        Account actor = accountRepository.findByIdAndDeletedAtIsNull(actorId).orElseThrow(() ->
                new AccountNotFoundException(id));

        Account account = get(id);
        account.softDelete(actor.getId());
    }

}
