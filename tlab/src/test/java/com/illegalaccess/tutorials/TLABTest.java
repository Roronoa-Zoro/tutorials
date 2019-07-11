package com.illegalaccess.tutorials;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TLABTest {

    @Test
    public void threadLocalTest() throws InterruptedException {
        TimeUnit.MINUTES.sleep(3L);
        ThreadLocal<List<String>> tl = new ThreadLocal<>();
        tl.set(new ArrayList<>());
        for (int i = 0; i < 300; i++) {
            List<String> list = tl.get();
            list.add(UUID.randomUUID().toString());
        }
        System.out.println("done..............." + tl.get().size() + "bytes");
        TimeUnit.MINUTES.sleep(5L);
    }

    @Test
    public void cTest() throws UnsupportedEncodingException {
        System.out.println("done..............." + UUID.randomUUID().toString().getBytes("utf-8").length * 300 + "bytes");
    }
}
