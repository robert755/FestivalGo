package com.server.backend.chat;

import com.server.backend.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatGroupController {

    @Autowired
    private ChatGroupService chatGroupService;

    @Autowired
    private MessageService messageService;

    // Creează un grup nou
    @PostMapping("/groups/create")
    public ResponseEntity<ChatGroup> createGroup(@RequestParam String name, @RequestParam Integer creatorId) {
        ChatGroup group = chatGroupService.createGroup(name, creatorId);
        return ResponseEntity.ok(group);
    }

    // Adaugă un user într-un grup
    @PostMapping("/groups/{groupId}/add")
    public ResponseEntity<String> addUserToGroup(@PathVariable Integer groupId, @RequestParam Integer userId) {
        String message = chatGroupService.addMemberToGroup(groupId, userId);
        return ResponseEntity.ok(message);
    }

    // Trimite un mesaj într-un grup
    @PostMapping("/groups/{groupId}/message")
    public ResponseEntity<Message> sendMessage(@PathVariable Integer groupId,
                                               @RequestParam Integer senderId,
                                               @RequestParam String text) {
        Message message = messageService.sendMessage(groupId, senderId, text);
        return ResponseEntity.ok(message);
    }

    // Afișează toate mesajele dintr-un grup
    @GetMapping("/groups/{groupId}/messages")
    public ResponseEntity<List<Message>> getMessages(@PathVariable Integer groupId) {
        List<Message> messages = messageService.getMessagesForGroup(groupId);
        return ResponseEntity.ok(messages);
    }

    // Afișează toate grupurile în care este un user
    @GetMapping("/groups/user/{userId}")
    public ResponseEntity<List<ChatGroup>> getGroupsForUser(@PathVariable Integer userId) {
        List<ChatGroup> groups = chatGroupService.getGroupsForUser(userId);
        return ResponseEntity.ok(groups);
    }
}
