package lesson01.start;

public class HelloRunnable implements Runnable{

    @Override
    public void run() {
        System.out.println("Hello Daemon Thread : run() " + Thread.currentThread().getName());
    }

}
