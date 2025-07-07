package lesson01.start;

import static lesson01.utils.MyLogger.log;

import lesson01.start.InnerRunnableMainV1.MyRunnable;

public class InnerRunnableMainV2 {

    public static void main(String[] args) {
        log("main() start");

        MyRunnable myRunnable = new MyRunnable() {
            @Override
            public void run() {
                log("run");
            }
        };

        Thread thread = new Thread(myRunnable);
        thread.start();

        log("main() end");
    }


}
