package org.iptime.yoon.blog.user.entity;

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
@Where(clause = "deleted = false")
@Table
public class BlogUser extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Setter
    private String password;

    @Setter
    private String profile;

    @Setter
    private LocalDate dateOfBirth;

    public int getAge(){
        if(dateOfBirth != null && LocalDate.now().isAfter(dateOfBirth)){
            return Period.between(dateOfBirth, LocalDate.now()).getYears();
        }
        return -1;
    }

    public String getEmail(){
        return this.username;
    }

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private BlogRole role = BlogRole.USER;
}
