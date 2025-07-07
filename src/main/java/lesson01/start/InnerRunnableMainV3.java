package lesson01.start;

import static lesson01.utils.MyLogger.log;

import lesson01.start.InnerRunnableMainV1.MyRunnable;

public class InnerRunnableMainV3 {

    public static void main(String[] args) {
        log("main() start");

        Thread thread = new Thread(new MyRunnable() {
            @Override
            public void run() {
                log("run");
            }
        });
        thread.start();

        log("main() end");
    }


}
