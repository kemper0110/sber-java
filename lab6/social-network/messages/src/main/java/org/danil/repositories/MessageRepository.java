package org.danil.repositories;

import org.danil.domain.Message;

import java.util.Date;

public interface MessageRepository {
    Message save(Message message);
    void deleteById(Long id);
    void getLastMessagesFrom(Date from);
}
