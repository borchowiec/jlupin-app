package com.example.service.impl;

import com.example.service.interfaces.SampleChannelWriter;
import com.jlupin.impl.client.util.channel.JLupinClientChannelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sampleChannelWriter")
public class SampleChannelWriterImpl implements SampleChannelWriter {

    @Autowired
    private JLupinClientChannelUtil jLupinClientChannelUtil;

    @Override
    public String openStreamChannelAndStartWriteToIt() throws Throwable {
        String streamChannelId = jLupinClientChannelUtil.openStreamChannel();
        for(int i = 0 ; i < 5; i ++) {
            jLupinClientChannelUtil.putNextElementToStreamChannel(streamChannelId, new Integer(i));
        }
        jLupinClientChannelUtil.closeStreamChannel(streamChannelId);
        return streamChannelId;
    }
}
