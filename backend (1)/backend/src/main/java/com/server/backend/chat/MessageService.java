package com.server.backend.chat;

import com.server.backend.user.User;
import com.server.backend.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatGroupRepository chatGroupRepository;

    @Autowired
    private UserRepository userRepository;

    public Message sendMessage(Integer groupId, Integer senderId, String text) {
        ChatGroup group = chatGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!group.getMembers().contains(sender)) {
            throw new RuntimeException("User is not a member of this group");
        }

        Message message = new Message(sender, group, text);
        return messageRepository.save(message);
    }

    public List<Message> getMessagesForGroup(Integer groupId) {
        return messageRepository.findByGroupIdOrderByTimestampAsc(groupId);
    }
}
