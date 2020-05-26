package com.example.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification implements Serializable {
    private String receiver;
    private String sender;
    private NotificationType type;
    private String content;
}
