package org.example;

public class MyTask extends Thread{
    @Override
    public void run() {
        super.run();
        System.out.println("Thread started");
    }
}
