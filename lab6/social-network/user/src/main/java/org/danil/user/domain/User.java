package org.danil.user.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    final private Long id;

    private String email;
    private String firstname;
    private String lastname;

    private Integer age;
    private Boolean sex;

    private String avatar;

    private String password;
}
