package org.danil.friends.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.danil.user.domain.User;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class Friendship {
    private User first, second;
    private Date createdAt;
    public enum Status {
        REQUESTED, ACTIVE
    }
    private Status status;
}
