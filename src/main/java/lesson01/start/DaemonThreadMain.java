package lesson01.start;

public class DaemonThreadMain {

    public static void main(String[] args) {
        Thread nowThread = Thread.currentThread();
        System.out.println("main() start " + nowThread.getName());

        DaemonThread daemonThread = new DaemonThread();
        daemonThread.setDaemon(true); // 데몬스레드 여부 설정
        daemonThread.start();

        System.out.println("main() end " + nowThread.getName());
    }

    static class DaemonThread extends Thread {

        @Override
        public void run() {
            System.out.println("Hello Daemon Thread : START" + Thread.currentThread().getName());

            try {
                Thread.sleep(10000); // 10초간 실행
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Hello Daemon Thread : END " + Thread.currentThread().getName());
        }
    }


}
