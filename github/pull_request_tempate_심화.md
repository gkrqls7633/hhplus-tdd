### **커밋 링크**


커밋 이름 : 
<1. 통합 테스트 코드 작성 및 Pointservice Inmemory 리팩토링>
커밋 링크 : https://github.com/gkrqls7633/hhplus-tdd/commit/a238e4a4a57445ef35fe9b801e68738b9c98e8e3


<2. 동시성 케이스 실패 테스트 작성>
커밋 링크 : https://github.com/gkrqls7633/hhplus-tdd/commit/74cb7ffbc54b9b7b23bfb06aa0d86af52ab5fe4e


<3. 동시성 스레드 처리 syncronized 처리 및 통합테스트 작성>
커밋 링크 : https://github.com/gkrqls7633/hhplus-tdd/commit/0c91ee25e4263bc0e2f145cfb2b24daa753ba563



---
### **리뷰 포인트(질문)**
- 리뷰 포인트 1 : 
동시성 이슈 처리 방안으로 저는  syncronized를 택했습니다. 여러 쓰레드를 동시에 생성하여 Lock, unLock 시점을 측정하고 랜덤 throttle 타임이 종료되면 다음 쓰레드가 실행될 수 있도록 처리했습니다.
Syncronized 처리가 없으면 쓰레드가 동시에 호출되었을 때, 포인트의 충전 및 사용이 동시에 진행되면서 충전/차감 처리가 1회만 되는 경우를 확인했습니다. (동시성 케이스 실패 테스트 작성 커밋)
해당 처리를 방지하고자 syncronized를 활용했는데, 0c91ee 커밋 내역에서 PointServiceIntegrationtest.java의 작성 내용이 잘 구성된 테스트 인지 궁금합니다.


- 리뷰 포인트 2 : 
석범코치님의 멘토링 타임에 injectMock에 대한 질문을 드렸을 때, 해당 어노테이션은 통합테스트에서 사용하는 것이라고 했습니다.
해당 어노테이션은 단위테스트에선 사용이 불가능한지, 불가능하다면 왜그런지 궁금합니다.
저는 통합테스트에서 injectMock이 아닌, autowired로 통합적인 의존성을 주입해야 한다고 생각하여 단순 mock에 의존성을 주입하는 것이 아닌 autowired를 사용했습니다. 옳바른 방향인지 확인받고 싶습니다.
  

-리뷰 포인트 3 :
통합테스트 클래스 파일을 별도로 만들어서(PointServiceIntegrationtest.java) 통합테스트 코드를 작성했습니다.
단위테스트는 PointServiceTest.java 에서만 구현했습니다.
이런식으로 파일을 별도로 구분지어 단위테스트 용도 / 통합테스트 용도 파일로 테스트를 진행하는 것이 맞는지 궁금합니다.
하나의 java 파일에서도 가능한지도 궁금합니다.



---
### **이번주 KPT 회고**

### Keep
<!-- 유지해야 할 좋은 점 -->
통합테스트 작성 시 beforeEach의 명확한 쓰임새를 이해하고 사용하자.

### Problem
<!--개선이 필요한 점-->
단위테스트와 통합테스트의 구분을 확실히 이해하고 넘어가자. 
injectMock, Autowired의 쓰임새를 확실히 이해해보자.


### Try
<!-- 새롭게 시도할 점 -->
Syncronized 이외이 동시성 이슈 처리의 다양한 방안에 대해 고민해보자.
