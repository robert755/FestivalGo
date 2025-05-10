package com.server.backend.chat;

import com.server.backend.user.User;
import com.server.backend.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatGroupService {

    @Autowired
    private ChatGroupRepository chatGroupRepository;

    @Autowired
    private UserRepository userRepository;

    public ChatGroup createGroup(String name, Integer creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ChatGroup group = new ChatGroup();
        group.setName(name);
        group.getMembers().add(creator);

        return chatGroupRepository.save(group);
    }

    public String addMemberToGroup(Integer groupId, Integer userId) {
        ChatGroup group = chatGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!group.getMembers().contains(user)) {
            group.getMembers().add(user);
            chatGroupRepository.save(group);
        }

        return "User added to group";
    }

    public List<ChatGroup> getGroupsForUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ChatGroup> allGroups = chatGroupRepository.findAll();
        List<ChatGroup> userGroups = new ArrayList<>();

        for (ChatGroup group : allGroups) {
            if (group.getMembers().contains(user)) {
                userGroups.add(group);
            }
        }

        return userGroups;
    }
}
