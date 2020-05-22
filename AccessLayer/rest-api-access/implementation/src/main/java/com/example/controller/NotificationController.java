package com.example.controller;

import com.example.service.interfaces.SampleChannelWriter;
import com.jlupin.impl.client.util.channel.JLupinClientChannelIterableProducer;
import com.jlupin.impl.client.util.channel.JLupinClientChannelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @Autowired
    private SampleChannelWriter sampleChannelWriter;

    @Autowired
    private JLupinClientChannelIterableProducer jLupinClientChannelIterableProducer;

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @GetMapping("/list")
    public int list() throws Throwable {
        final String streamId = sampleChannelWriter.openStreamChannelAndStartWriteToIt();
        final Iterable iterable = jLupinClientChannelIterableProducer.produceChannelIterable("SAMPLE", streamId);

        int sum = 0;
        for (Object number : iterable) {
            sum += (int) number;
        }
        return sum;
    }
}
