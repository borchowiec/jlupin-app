package com.example.bean.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChannelContext {
    private String channelId;
    private long userId;
    private String sessionId;
}
