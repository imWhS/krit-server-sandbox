package imwhs.krit_server_sandbox.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class AccountTest {

    @Test
    void 계정을_생성할_수_있다() {
        // given
        String handle = "hee_x1ory";
        String email = "heextory@icloud.com";
        String encodedPassword = "encodedPassword";

        // when
        Account account = Account.create(handle, email, encodedPassword);

        // then
        Assertions.assertThat(account.getHandle()).isEqualTo(handle);
        Assertions.assertThat(account.getEmail()).isEqualTo(email);
        Assertions.assertThat(account.getEncodedPassword()).isEqualTo(encodedPassword);
    }

    @Test
    void 계정_생성_시_핸들에_대문자를_사용할_수_없다() {
        // given
        String invalidHandle = "HEE_x1ory";
        String email = "heextory@icloud.com";
        String encodedPassword = "encodedPassword";

        // when, then
        Assertions
                .assertThatThrownBy(
                        () -> Account.create(invalidHandle, email, encodedPassword)
                )
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 계정_생성_시_핸들에_한글을_사용할_수_없다() {
        // given
        String invalidHandle = "HEE_스토리";
        String email = "heextory@icloud.com";
        String encodedPassword = "encodedPassword";

        // when, then
        Assertions
                .assertThatThrownBy(
                        () -> Account.create(invalidHandle, email, encodedPassword)
                )
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 계정_생성_시_핸들에_특수문자를_사용할_수_없다() {
        // given
        String invalidHandle = "HEE_x1!ory";
        String email = "heextory@icloud.com";
        String encodedPassword = "encodedPassword";

        // when, then
        Assertions
                .assertThatThrownBy(
                        () -> Account.create(invalidHandle, email, encodedPassword)
                )
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 계정을_논리적으로_삭제할_수_있다() {
        // given
        String handle = "heextory";
        String email = "heextory@icloud.com";
        String encodedPassword = "encodedPassword";

        Account account = Account.create(handle, email, encodedPassword);

        Long actorId = 2L;

        // when
        account.softDelete(actorId);

        // then
        Assertions.assertThat(account.isDeleted()).isTrue();
        Assertions.assertThat(account.getDeletedAt()).isNotNull();
        Assertions.assertThat(account.getDeletedBy()).isEqualTo(actorId);
    }

    @Test
    void 이미_삭제한_계정을_다시_삭제해도_멱등성이_보장된다() {
        // given
        String handle = "heextory";
        String email = "heextory@icloud.com";
        String encodedPassword = "encodedPassword";

        Account account = Account.create(handle, email, encodedPassword);
        Long firstActorId = 2L;
        Long lastActorId = 9L;

        account.softDelete(firstActorId);
        LocalDateTime firstDeletedAt = account.getDeletedAt();
        Long firstDeletedBy = account.getDeletedBy();

        // when
        account.softDelete(lastActorId);

        // then
        Assertions.assertThat(account.getDeletedBy()).isEqualTo(firstDeletedBy);
        Assertions.assertThat(account.getDeletedAt()).isEqualTo(firstDeletedAt);
    }

}
