package org.iptime.yoon.blog.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import org.iptime.yoon.blog.common.entity.BaseEntity;

import java.time.LocalDate;
import java.time.Period;

/**
 * @author rival
 * @since 2023-08-11
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Where(clause = "deleted = false")
@Table(
    name = "blog_user",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"provider","subject"})})
@Setter
public class BlogUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String profile;


    private Long profileImageId;

    private LocalDate dateOfBirth;

    private String displayName;

    private String email;


    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name="provider")
    private AuthProvider provider = AuthProvider.LOCAL;

    // OAuth2
    @Column(name="subject")
    private String subject;

    public int getAge(){
        if(dateOfBirth != null && LocalDate.now().isAfter(dateOfBirth)){
            return Period.between(dateOfBirth, LocalDate.now()).getYears();
        }
        return -1;
    }

    public boolean isLocalUser(){
        return AuthProvider.LOCAL.equals(this.provider);
    }

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private BlogRole role = BlogRole.USER;
}
