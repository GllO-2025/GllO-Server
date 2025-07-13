package backend.glloserver.member.repository.entity;

import backend.glloserver.member.domain.AuthProvider;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
@Entity
@Getter
@Builder
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    @NotNull
    private String password;

    private String email;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String loginId;

}
