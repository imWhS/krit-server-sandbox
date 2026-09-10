package imwhs.krit_server_sandbox.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Account extends BaseEntity {

    /**
     * 식별자 및 데이터베이스 기본 키(PK)
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 공개 식별자
     *
     * <ul>
     *     <li> 서비스 내에서 공개적으로 사용자를 식별할 수 있는 문자열입니다. </li>
     *     <li> 다른 계정과 중복될 수 없습니다. </li>
     *     <li> 최소 6자부터 최대 20자 이하 길이의 영어 알파벳 소문자, 밑줄(_), 마침표(.)만 사용할 수 있습니다.  </li>
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
     * 암호화된 암호
     */
    @Column(nullable = false)
    private String password;

    /**
     * 이미지 URL

     * <ul>
     *     <li> 사용자 생성 콘텐츠 등에서 작성자 프로필을 표시할 때 사용합니다. </li>
     * </ul>
     */
    private String imageUrl;

    private LocalDateTime deletedAt;

    private Long deletedBy;

    /**
     * 논리적으로 삭제합니다.
     * @param requesterId 삭제를 요청한 계정의 ID
     */
    public void softDelete(Long requesterId) {
        // 이미 논리적 삭제된 상태여도 멱등하게 처리합니다.
        if (deletedAt != null || deletedBy != null) { return; }
        deletedAt = LocalDateTime.now();
        deletedBy = requesterId;
    }

}
