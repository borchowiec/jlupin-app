package com.example.controller;

import com.example.common.pojo.AddMessageRequest;
import com.example.common.pojo.Notification;
import com.example.common.pojo.Response;
import com.example.common.util.JwtTokenProvider;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    private JwtTokenProvider tokenProvider = JwtTokenProvider.getInstance();

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @PostMapping("/add-message")
    public ResponseEntity<?> addMessage(@RequestBody @Valid AddMessageRequest request,
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
                    blockingMap.put(taskId, new Response<>(throwable, HttpStatus.INTERNAL_SERVER_ERROR));
                }
            }
        );

        Response response = (Response) blockingMap.get(taskId);
        if (response.getStatus() == HttpStatus.OK) {
            String principalId = tokenProvider.getId(token);
            Notification notification = new Notification(request.getReceiver(), principalId,
                    MESSAGE, request.getContent());
            notificationService.sendNotification(notification);
        }
        return new ResponseEntity<>(response.getPayload(), response.getStatus());
    }

    @GetMapping("/conversation/{interlocutorId}")
    public ResponseEntity<?> getConversation(@PathVariable String interlocutorId,
                                        @RequestHeader("Authorization") String token) {
        Response<?> response = messageService.getConversation(interlocutorId, token);
        return new ResponseEntity<>(response.getPayload(), response.getStatus());
    }

    @GetMapping("/interlocutors")
    public ResponseEntity<?> getInterlocutors(@RequestHeader("Authorization") String token) {
        Response<?> response = messageService.getInterlocutors(token);
        return new ResponseEntity<>(response.getPayload(), response.getStatus());
    }
}
