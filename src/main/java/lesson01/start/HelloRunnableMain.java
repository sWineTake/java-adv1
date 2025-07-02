package lesson01.start;

public class HelloRunnableMain {

    public static void main(String[] args) {
        Thread nowThread = Thread.currentThread();
        System.out.println("main() start " + nowThread.getName());

        HelloRunnable helloRunnable = new HelloRunnable();
        Thread thread = new Thread(helloRunnable);
        thread.start();

        System.out.println("main() end " + nowThread.getName());
    }

}
