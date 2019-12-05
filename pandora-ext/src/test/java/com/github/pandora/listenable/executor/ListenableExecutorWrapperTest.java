package com.github.pandora.listenable.executor;

import com.github.pandora.listenable.future.ListenableFuture;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class ListenableExecutorWrapperTest {

    @Test
    public void newTaskFor() {

    }

    @Test
    public void newTaskFor1() {
    }

    @Test
    public void submit() throws InterruptedException {

        String mainName = Thread.currentThread().getName();

        ExecutorService single = Executors.newSingleThreadExecutor(r -> new Thread(r, "single"));

        ListenableExecutor executor = ListenableExecutor.create(single);

        executor.execute(() -> {
            Assert.assertEquals(Thread.currentThread().getName(), "single");
        });


        ListenableFuture<Void> future = executor.submit(() -> {});
        Thread.sleep(500L); //等待submit提交的callable先执行完，那么addHandler 回调执行一定在main线程
        future.addHandler(v -> {
            System.out.println(Thread.currentThread().getName());
            Assert.assertEquals(mainName, Thread.currentThread().getName());
        });

        System.out.println("=================");

        executor.submit(() -> {
            System.out.println(Thread.currentThread().getName());
            Assert.assertEquals(Thread.currentThread().getName(), "single");
            try {
                Thread.sleep(1000L); //这里sleep，让下面的addHandler先执行，那么addHandler 回调执行一定在Single线程
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).addHandler(ar -> {
            System.out.println(Thread.currentThread().getName());
            Assert.assertEquals("single", Thread.currentThread().getName());
        });

        Thread.sleep(3000);

    }

    @Test
    public void execute() {
        //ignore
    }

    @Test
    public void delegate() {
        //ignore
    }
}