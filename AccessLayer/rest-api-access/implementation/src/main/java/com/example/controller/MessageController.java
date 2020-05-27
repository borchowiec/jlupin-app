package com.example.controller;

import com.example.common.pojo.AddMessageRequest;
import com.example.common.pojo.Conversation;
import com.example.common.pojo.Notification;
import com.example.common.pojo.UserInfo;
import com.example.service.interfaces.MessageService;
import com.example.service.interfaces.NotificationService;
import com.jlupin.impl.client.util.queue.JLupinClientQueueUtil;
import com.jlupin.impl.client.util.queue.exception.JLupinClientQueueUtilException;
import com.jlupin.impl.util.map.JLupinBlockingMap;
import com.jlupin.interfaces.function.JLupinQueueReactiveFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.example.common.pojo.NotificationType.MESSAGE;

@RestController
public class MessageController {

    @Autowired
    @Qualifier("notificationService")
    private NotificationService notificationService;

    @Autowired
    @Qualifier("messageService")
    private MessageService messageService;

    @Autowired
    @Qualifier("messagesQueueClientUtil")
    private JLupinClientQueueUtil messagesQueueClientUtil;

    @Autowired
    private JLupinBlockingMap blockingMap;

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @PostMapping("/add-message")
    public boolean addMessage(@RequestBody @Valid AddMessageRequest request,
                                        @RequestHeader("Authorization") String token) throws JLupinClientQueueUtilException {
        final String taskId = messagesQueueClientUtil.putTaskInput(
                "message",
                "messageService",
                "addMessage",
                new Object[] {request, token}
        );

        blockingMap.ensureBlockingContainer(taskId);
        messagesQueueClientUtil.registerFunctionOnTaskResult(
            taskId,
            new JLupinQueueReactiveFunction() {
                @Override
                public void onSuccess(final String taskId, final Object result) {
                    blockingMap.put(taskId, result);
                }

                @Override
                public void onError(final String taskId, final Throwable throwable) {
                    blockingMap.put(taskId, false);
                }
            }
        );

        boolean success = (boolean) blockingMap.get(taskId);
        if (success) {
            Notification notification = new Notification(request.getReceiver(), null, MESSAGE, request.getContent());
            notificationService.sendNotification(notification);
        }
        return success;
    }

    @GetMapping("/conversation/{interlocutorId}")
    public Conversation getConversation(@PathVariable String interlocutorId,
                                        @RequestHeader("Authorization") String token) {
        return messageService.getConversation(interlocutorId, token);
    }

    @GetMapping("/interlocutors")
    public List<UserInfo> getInterlocutors(@RequestHeader("Authorization") String token) {
        return messageService.getInterlocutors(token);
    }
}
