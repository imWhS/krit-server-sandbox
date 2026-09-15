package imwhs.krit_server_sandbox.service;

import imwhs.krit_server_sandbox.domain.Account;
import imwhs.krit_server_sandbox.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

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
            throw new IllegalStateException("이미 " + handle + "을 공개 식별자(핸들)로 사용하고 있는 계정이 있어요.");
        }

        boolean isExistByEmail = accountRepository.existsByEmailAndDeletedAtIsNull(email);
        if (isExistByEmail) {
            throw new IllegalStateException(email + "은 이미 다른 계정에서 사용하고 있는 이메일 주소예요.");
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

        Account account = accountRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("ID가 " + id + "인 계정을 찾을 수 없어요."));

        return account;
    }

    public Account getByHandle(String handle) {
        if (handle == null || handle.isBlank()) {
            throw new IllegalArgumentException("계정의 공개 식별자(핸들)가 유효하지 않아요.");
        }

        Account account = accountRepository.findByHandleAndDeletedAtIsNull(handle).orElseThrow(() ->
                new NoSuchElementException("핸들(" + handle + ")에 해당하는 계정을 찾을 수 없어요."));

        return account;
    }

    @Transactional
    public void updateName(String handle, String name) {
        Account account = getByHandle(handle);
        account.updateName(name);
    }

    @Transactional
    public void updateImageUrl(String handle, String imageUrl) {
        Account account = getByHandle(handle);
        account.updateImageUrl(imageUrl);
    }

    @Transactional
    public void softDelete(String handle, Long actorId) {
        Account actor = accountRepository.findById(actorId).orElseThrow(() ->
                new NoSuchElementException("계정 삭제를 요청한 계정의 ID가 유효하지 않아요."));

        Account account = getByHandle(handle);
        account.softDelete(actor.getId());
    }

}
