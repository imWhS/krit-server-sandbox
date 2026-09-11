package imwhs.krit_server_sandbox.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Account extends BaseEntity {

    private static final Pattern HANDLE_PATTERN = Pattern.compile("^[a-z0-9_.]{6,20}$");

    /**
     * 식별자 및 데이터베이스 기본 키(PK)
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 공개 식별자(핸들)
     *
     * <ul>
     *     <li> 서비스 내에서 공개적으로 사용자를 식별할 수 있는 문자열입니다. </li>
     *     <li> 다른 계정과 중복될 수 없습니다. </li>
     *     <li> 최소 6자부터 최대 20자 이하 길이의 영어 알파벳 소문자, 숫자, 밑줄(_), 마침표(.)만 사용할 수 있습니다.  </li>
     * </ul>
     */
    @Column(length = 20, unique = true, nullable = false)
    private String handle;

    /**
     * 이메일 주소
     *
     * <ul>
     *     <li> 계정 로그인 및 이메일 기반 기능에서 사용합니다. </li>
     * </ul>
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * 해싱된 암호
     */
    @Column(nullable = false)
    private String encodedPassword;

    /**
     * 이름
     *
     * <ul>
     *     <li> 사용자 생성 콘텐츠 등에서 작성자 프로필을 표시할 때 사용합니다. </li>
     *     <li> 최소 2자부터 최대 40자 이하 길이의 문자열을 사용할 수 있습니다. </li>
     * </ul>
     */
    @Column(length = 40)
    private String name;

    /**
     * 이미지 URL
     *
     * <ul>
     *     <li> 사용자 생성 콘텐츠 등에서 작성자 프로필을 표시할 때 사용합니다. </li>
     * </ul>
     */
    private String imageUrl;

    private LocalDateTime deletedAt;

    private Long deletedBy;

    private Account(String handle, String email, String encodedPassword) {
        this.handle = handle;
        this.email = email;
        this.encodedPassword = encodedPassword;
    }

    public Account updateName(String name) {
        validateName(name);
        this.name = name;
        return this;
    }

    public Account removeName() {
        this.name = null;
        return this;
    }

    public Account updateImageUrl(String imageUrl) {
        validateImageUrl(imageUrl);
        this.imageUrl = imageUrl;
        return this;
    }

    public Account removeImageUrl() {
        this.imageUrl = null;
        return this;
    }

    /**
     * 논리적 삭제 여부를 나타냅니다.
     * @return 논리적 삭제 여부
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }

    /**
     * 논리적으로 삭제합니다.
     * @param deletedBy 삭제를 요청한 계정의 ID
     */
    public void softDelete(Long deletedBy) {
        // 이미 논리적 삭제된 상태여도 멱등하게 처리합니다.
        if (isDeleted()) { return; }
        deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }

    public static Account create(String handle, String email, String encodedPassword) {
        validateHandle(handle);
        validateEmail(email);
        validateEncodedPassword(encodedPassword);
        return new Account(handle, email, encodedPassword);
    }

    private static void validateHandle(String handle) {
        if (handle == null || handle.isBlank()) {
            throw new IllegalArgumentException("계정의 공개 식별자(핸들)가 유효하지 않아요.");
        }

        if (!HANDLE_PATTERN.matcher(handle).matches()) {
            throw new IllegalArgumentException("계정의 공개 식별자(핸들)는 최소 6자부터 최대 20자 이하 길이의 영어 알파벳 소문자, 숫자, 밑줄(_), 마침표(.)만 사용할 수 있어요.");
        }
    }

    private static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("계정의 이메일 주소가 유효하지 않아요.");
        }
    }

    private static void validateEncodedPassword(String encodedPassword) {
        if (encodedPassword == null || encodedPassword.isBlank()) {
            throw new IllegalArgumentException("계정의 암호가 유효하지 않아요.");
        }
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("계정의 이름이 유효하지 않아요.");
        }

        if (name.length() < 2 || 40 < name.length()) {
            throw new IllegalArgumentException("계정의 이름은 최소 2자부터 최대 40자 이하 길이의 문자열만 사용할 수 있어요.");
        }
    }

    private static void validateImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("계정의 이미지 URL이 유효하지 않아요.");
        }
    }

}
