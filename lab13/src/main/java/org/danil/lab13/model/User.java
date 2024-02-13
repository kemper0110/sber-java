package org.danil.lab13.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class User {
    @Id
    private Long id;

    private String firstname;
    private String lastname;
    private String job;
    private String mail;
    private String phone;
    private String image;
}
