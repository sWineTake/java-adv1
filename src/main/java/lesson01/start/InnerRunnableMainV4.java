package lesson01.start;

import static lesson01.utils.MyLogger.log;

public class InnerRunnableMainV4 {

    public static void main(String[] args) {
        log("main() start");

        Thread thread = new Thread(() -> log("run"));
        thread.start();

        log("main() end");
    }


}
