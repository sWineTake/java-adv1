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
