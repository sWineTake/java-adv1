package lesson01.start;

public class BadThreadMain {

    public static void main(String[] args) {
        // 메인 메소드 실행시 - 별도의 스택공간에 main스레드가 실행

        // 메인 쓰레드가 실행
        Thread nowThread = Thread.currentThread();
        System.out.println("main() start " + nowThread.getName());

        HelloThread helloThread = new HelloThread();
        System.out.println("helloThread() start 호출 전" + helloThread.getName());
        // 실제 스택공간에 별도의 스레드 생성하면서 작동(main 스레드가 실행안함)
        // main 스레드가 별도의 스레드에게 start를 실행하라는 명령만 지시하고, 실제 실행은 별도의 스레드에서 start 메소드를 실행함
        helloThread.run(); // 만약 run 메소드가 직접 호출시 main쓰레드가 직접 run을 호출하게된다.
        System.out.println("helloThread() start 호출 후" + helloThread.getName());

        System.out.println("main() end " + nowThread.getName());

    }

}
