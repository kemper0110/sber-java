package org.danil.services;

import org.danil.domain.requests.DeleteMessageRequest;
import org.danil.domain.requests.SendMessageRequest;
import org.danil.domain.requests.UpdateMessageRequest;
import org.danil.exceptions.MessageException;

public interface MessageService {
    void sendMessage(SendMessageRequest request) throws MessageException;
    void deleteMessage(DeleteMessageRequest request) throws MessageException;
    void updateMessage(UpdateMessageRequest request) throws MessageException;
}
