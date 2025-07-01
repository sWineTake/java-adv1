package lesson01.start;

public class HelloThread extends Thread {

    @Override
    public void run() {
        System.out.println("HelloThread : 실행" + Thread.currentThread().getName());
    }

}
