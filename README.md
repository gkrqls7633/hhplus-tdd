# hhplus-tdd

## 1주차 : TDD에 대한 명확한 이해와 동시성 제어 방식의 이해


## 동시성 제어 방식 및 각 적용의 장/단점
- 선택 동시성 제어 방식 : Syncronized
- Synchronized는 Java에서 동시성 제어 방식으로, 특정 코드 블록이나 메서드에 대해 하나의 스레드만 접근할 수 있도록 보장한다.
- 이를 통해 멀티스레딩 환경에서 데이터의 일관성과 안전성을 확보할 수 있다.

### 장점
1. 간단한 구현: synchronized 키워드를 이용해 간단히 동기화를 구현할 수 있다.
2. 일관성 보장: 여러 스레드가 동일 자원에 동시에 접근하지 않도록 하여 데이터 일관성을 유지한다.
3. 자동화된 락 관리: 락을 자동으로 관리해주기 때문에 개발자가 직접 락을 관리할 필요가 없다.

### 단점:
1. 성능 저하: 락을 획득하고 해제하는 데 오버헤드가 발생하여 성능이 저하될 수 있다.
2. 교착 상태(Deadlock): 여러 스레드가 서로 기다리게 되는 교착 상태가 발생할 위험이 있다.
3. 제한된 확장성: 여러 스레드가 동시에 실행되는 경우 성능 저하가 발생할 수 있으며, 자원을 효율적으로 활용하기 어려울 수 있다.

