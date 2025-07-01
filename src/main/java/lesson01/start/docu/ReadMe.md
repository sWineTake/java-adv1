# 자바 메모리 구조

자바의 메모리는 크게 **메서드**, **스택**, **힙** 영역으로 구분됩니다.

## 📋 메서드 영역 (Method Area)
- 프로그램 실행에 필요한 공통 데이터를 관리
- **저장 내용:**
  - 클래스 정보
  - static 변수
  - 리터럴 상수

## 📚 스택 영역 (Stack Area)
- **저장 내용:**
  - 지역 변수
  - 중간 연산 결과
  - 메서드 호출 정보
- **특징:**
  - 메서드 호출 시마다 하나의 스택 프레임이 생성됨
  - 메서드 종료 시 해당 스택 프레임이 제거됨
  - 각 스레드별로 독립적인 실행 스택이 생성됨 (스레드 수만큼 스택 존재)

## 🏠 힙 영역 (Heap Area)
- **저장 내용:**
  - 객체와 배열이 생성되는 영역
- **특징:**
  - 가비지 컬렉션(GC)이 이루어지는 주요 영역
  - 더 이상 참조되지 않는 객체는 GC에 의해 자동으로 제거됨

---

# 🧵 스레드와 메모리 구조 예제

## 예제 코드

### HelloThread.java
```java
package lesson01.start;

public class HelloThread extends Thread {
    @Override
    public void run() {
        System.out.println("HelloThread : 실행" + Thread.currentThread().getName());
    }
}
```

### HelloThreadMain.java
```java
package lesson01.start;

public class HelloThreadMain {
    public static void main(String[] args) {
        // 메인 메소드 실행시 - 별도의 스택공간에 main스레드가 실행
        Thread nowThread = Thread.currentThread();
        System.out.println("main() start " + nowThread.getName());

        HelloThread helloThread = new HelloThread();
        System.out.println("helloThread() start 호출 전" + helloThread.getName());
        
        // 실제 스택공간에 별도의 스레드 생성하면서 작동
        helloThread.start(); // 별도의 스레드가 run 메소드를 실행
        System.out.println("helloThread() start 호출 후" + helloThread.getName());

        System.out.println("main() end " + nowThread.getName());
    }
}
```

## 🔍 메모리 구조 분석

### 스레드별 스택 영역
- **main 스레드**: 별도의 스택 공간에서 main() 메서드 실행
- **HelloThread**: `start()` 호출 시 새로운 스택 공간에서 `run()` 메서드 실행
- 각 스레드는 독립적인 스택 영역을 가짐

### 힙 영역
- `HelloThread helloThread = new HelloThread()`: HelloThread 객체가 힙 영역에 생성
- 모든 스레드가 힙 영역을 공유

### 메서드 영역
- HelloThread 클래스 정보가 메서드 영역에 저장
- Thread 클래스의 메서드들도 메서드 영역에 저장

## ⚡ 실행 흐름
1. **main 스레드**가 main() 메서드를 실행 (main 스레드의 스택에서)
2. HelloThread 객체를 **힙 영역**에 생성
3. `helloThread.start()` 호출 시 **새로운 스레드**가 생성되어 별도의 스택에서 `run()` 메서드 실행
4. main 스레드와 HelloThread가 **동시에** 실행됨

---

# ❌ 잘못된 스레드 사용 예제

## BadThreadMain.java
```java
package lesson01.start;

public class BadThreadMain {
    public static void main(String[] args) {
        Thread nowThread = Thread.currentThread();
        System.out.println("main() start " + nowThread.getName());

        HelloThread helloThread = new HelloThread();
        System.out.println("helloThread() start 호출 전" + helloThread.getName());
        
        // ❌ 잘못된 방법: run() 메서드를 직접 호출
        helloThread.run(); // main 스레드가 직접 run을 호출하게 됨
        System.out.println("helloThread() start 호출 후" + helloThread.getName());

        System.out.println("main() end " + nowThread.getName());
    }
}
```

## 🚨 문제점 분석

### run() 직접 호출의 문제
- `helloThread.run()`을 직접 호출하면 **새로운 스레드가 생성되지 않음**
- **main 스레드**가 직접 run() 메서드를 실행
- 별도의 스택 공간이 생성되지 않고 main 스레드의 스택에서 실행
- **멀티스레딩의 장점을 전혀 활용할 수 없음**

### 올바른 방법 vs 잘못된 방법

| 구분 | 올바른 방법 | 잘못된 방법 |
|------|-------------|-------------|
| **호출 방식** | `helloThread.start()` | `helloThread.run()` |
| **실행 스레드** | 새로운 스레드 생성 | main 스레드에서 실행 |
| **스택 영역** | 별도의 스택 공간 | main 스레드의 스택 |
| **동시 실행** | 가능 (멀티스레딩) | 불가능 (순차 실행) |
| **성능** | 병렬 처리 가능 | 단일 스레드 처리 |

## 💡 핵심 포인트
- **start() 메서드**: JVM이 새로운 스레드를 생성하고 해당 스레드에서 run() 메서드를 호출
- **run() 메서드**: 단순한 메서드 호출로, 현재 스레드에서 직접 실행
- 스레드의 진정한 활용을 위해서는 반드시 **start() 메서드를 사용**해야 함

---

# 👻 데몬 스레드 (Daemon Thread)

## 예제 코드

### DaemonThreadMain.java
```java
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
```

## 🔍 데몬 스레드 개념

스레드는 **사용자 스레드**와 **데몬 스레드** 2가지 종류로 구분됩니다.

### 👤 사용자 스레드 (User Thread)
- 프로그램의 **주요 작업을 수행**
- 작업이 완료될 때까지 실행
- **모든 사용자 스레드가 종료되면 JVM도 종료**

### 👻 데몬 스레드 (Daemon Thread)
- **백그라운드에서 보조적인 작업을 수행**
- 모든 사용자 스레드가 종료되면 **데몬 스레드는 자동으로 종료**
- 예: 가비지 컴팩터, 로그 처리, 모니터링 등

## 🔄 실행 결과 분석

### 데몬 스레드 설정 시
```
main() start main
Hello Daemon Thread : START Thread-0
main() end main
// 프로그램 즉시 종료 (10초를 기다리지 않음)
```

### 일반 스레드 설정 시
```
main() start main
Hello Daemon Thread : START Thread-0
main() end main
// 10초 대기 후...
Hello Daemon Thread : END Thread-0
// 프로그램 종료
```

## ⚙️ 데몬 스레드 설정 방법

```java
Thread thread = new Thread();
thread.setDaemon(true);  // 데몬 스레드로 설정
thread.start();          // start() 호출 전에 설정해야 함
```

## 🤔 왜 데몬 스레드가 함께 종료될까?

### 프로그램 생명주기 관점
- **사용자 스레드 = 프로그램의 주체**
- 모든 사용자 스레드 종료 = 프로그램의 주요 작업 완료
- JVM: "프로그램이 끝났으니 종료하자!"

### 데몬 스레드의 본질
```java
// 만약 데몬 스레드가 계속 살아있다면?
public static void main(String[] args) {
    System.out.println("메인 작업 완료");
    // 프로그램이 끝났는데...
}
// 데몬 스레드가 계속 실행 중... 🤷‍♂️
// → 프로그램이 언제 끝날지 모름!
```

### 실제 사례
- **웹서버 종료** → 로그 처리 스레드도 불필요
- **게임 종료** → 백그라운드 음악 스레드도 불필요
- **데이터베이스 연결 종료** → 모니터링 스레드도 불필요

### 핵심 포인트
- 데몬 스레드는 **"주인(사용자 스레드)을 따라다니는 하인"**
- 주인이 없으면 하인도 존재할 이유가 없음
- **프로그램의 깔끔한 종료**를 위한 자바의 설계 철학

## ⚠️ 주의사항
- `setDaemon(true)`는 **반드시 `start()` 호출 전에** 설정해야 함
- 데몬 스레드는 예상치 못하게 종료될 수 있으므로 **중요한 작업에는 사용 금지**
- 주로 **보조적인 작업**(모니터링, 로깅, 청소 작업 등)에 사용

---

# 🌱 스프링부트 Executor 스레드

## 📝 결론: 데몬 스레드가 아닙니다!

스프링부트의 Executor 스레드들은 **사용자 스레드**입니다.

## 🔍 스프링부트 스레드 구조

```java
// 스프링부트 내부적으로 이런 식으로 동작
@Async
public void asyncMethod() {
    // 이 메서드는 TaskExecutor의 스레드풀에서 실행
    // 이 스레드들은 사용자 스레드!
}
```

## 🤔 왜 사용자 스레드일까?

### 1. 웹 요청 처리
- HTTP 요청을 처리하는 것은 **주요 작업**
- 서버의 핵심 기능

### 2. 비즈니스 로직 실행
- @Async로 실행되는 작업들도 **핵심 기능**
- 데이터 처리, 이메일 발송 등

### 3. 서버의 생명 유지
- 웹서버가 살아있어야 하므로 스레드들이 계속 대기

## 📊 실제 확인해보기

```java
@RestController
public class TestController {
    
    @Async
    @GetMapping("/test")
    public void asyncTest() {
        Thread currentThread = Thread.currentThread();
        System.out.println("Thread name: " + currentThread.getName());
        System.out.println("Is daemon: " + currentThread.isDaemon()); // false!
        
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

## 👻 스프링부트에서 데몬 스레드를 찾는다면?

**실제 데몬 스레드들:**
- **GC 스레드**: 가비지 컴렉션
- **JVM 내부 스레드**: 클래스 로딩, JIT 컴파일러 등
- **모니터링 스레드**: JMX, 메트릭 수집 등

## 🔄 핵심 차이점

| 구분 | 스프링 Executor | 데몬 스레드 |
|------|----------------|-------------|
| **역할** | 주요 비즈니스 로직 처리 | 보조적 작업 |
| **생명주기** | 애플리케이션과 함께 | 사용자 스레드에 의존 |
| **종료 방식** | 명시적 shutdown 필요 | 자동 종료 |
| **중요도** | 핵심 기능 | 부가 기능 |

## 🚀 스프링부트의 안정성

```java
// 스프링부트 종료 시
@PreDestroy
public void shutdown() {
    taskExecutor.shutdown(); // 명시적으로 스레드풀 종료
    // 데몬 스레드였다면 이런 처리가 불필요했을 것
}
```

**스프링부트의 Executor 스레드들이 사용자 스레드인 이유:**
- 애플리케이션 종료 시 명시적으로 shutdown 해야 함
- graceful shutdown 과정에서 진행 중인 작업들을 완료할 시간을 줌
- 중요한 비즈니스 로직이 중간에 끔기지 않도록 보장

이게 바로 웹 애플리케이션이 안정적으로 동작할 수 있는 이유입니다! 🎆
