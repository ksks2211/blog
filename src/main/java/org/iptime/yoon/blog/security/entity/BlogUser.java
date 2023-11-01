package org.iptime.yoon.blog.security.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import org.iptime.yoon.blog.entity.Base;

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
@Setter
@Where(clause = "deleted = false")
public class BlogUser extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;
    private String email;
    private String profile;
    private LocalDate dateOfBirth;

    public int getAge(){
        if(dateOfBirth != null && LocalDate.now().isAfter(dateOfBirth)){
            return Period.between(dateOfBirth, LocalDate.now()).getYears();
        }
        return 0;
    }

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private BlogRole role = BlogRole.USER;
}
