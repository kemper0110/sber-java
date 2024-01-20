package org.danil.user.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private Long id;

    private String email;
    private String firstname;
    private String lastname;

    private Integer age;
    private Boolean sex;

    private String avatar;

    private String password;
}
