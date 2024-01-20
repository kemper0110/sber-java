package org.danil.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.danil.user.domain.User;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class Message {
    private Long id;
    private Date createdAt, updatedAt;

    private User sender;
    private User receiver;

    private String message;

}
