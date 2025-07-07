package lesson01.start;

import static lesson01.utils.MyLogger.log;

public class InnerRunnableMainV1 {

    public static void main(String[] args) {
        log("main() start");

        MyRunnable myRunnable = new MyRunnable();
        Thread thread = new Thread(myRunnable);
        thread.start();

        log("main() end");
    }

    public static class MyRunnable implements Runnable{

        @Override
        public void run() {
            log("run()");
        }

    }


}
