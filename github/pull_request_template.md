### **커밋 링크**


커밋 이름 : 
<1. 포인트충전>
커밋 링크 : https://github.com/gkrqls7633/hhplus-tdd/commit/63187565b48a749448afa3f538243b48d0920b2c
포인트 충전 기능 추가 및 테스트 코드 작성 : 6318756


<2. 포인트사용>
커밋 링크 : https://github.com/gkrqls7633/hhplus-tdd/commit/1636ba168248582e06cfbcdb6a1500bf7713c11f
포인트 사용 기능 추가 및 테스트 코드 작성 : 1636ba1 


<3. UserPointTable -> PointTempInmemory 대체>
커밋 링크 : https://github.com/gkrqls7633/hhplus-tdd/commit/ad4dcefe4d20addbc2fde23fca9b4a434c050303
PointTempInmemory 사용 : ad4dcef




---
### **리뷰 포인트(질문)**
- 리뷰 포인트 1 : UserPointTable의 존재를 확인하지 못하고 PointTempInmemory 사용하여 DB처럼 동작하게 구성하였습니다. 이에 누락된 점이 없는지, 로직이 적절한지 검토 부탁드립니다.(History의 경우 주어진 Table 클래스로 구현했습니다. 시간 관계상 PointTempInmemory로 구성한 채로 PR 진행하고 추후 리팩토링 검토하겠습니다.)
 (추가로,  PointTempInmemory를 발견한 시점에 PointTempInmemory -> UserPointTable 로의 마이그레이션 작업을 시도해 보았는데 이미 테스트 코드도 완성된 시점이고, return 받을 형식도 void 처리한 것들이 있어서 마이그레이션이 생각보다 번거롭다는 생각을 했는데 이는 객체지향적인 설계가 부족한 것 때문인지 궁금합니다.) 

- 리뷰 포인트 2 : Mockito.doAnswer의 사용에 미숙한 점이 있다고 생각합니다. 포인트 충전, 사용 관련 단위테스트에서 Mockito.doAnswer를 사용하여 mock처리한 port 인터페이스 객체가 해당 로직으로 동작하도록 구성했습니다. 해당 방식으로 사용하는 것이 맞는지 검토 부탁드립니다.

  
---
### **이번주 KPT 회고**

### Keep
<!-- 유지해야 할 좋은 점 -->
Mock과 Stub의 명확한 구분과 효율적인 사용으로 테스트 코드 작성하기.


### Problem
<!--개선이 필요한 점-->
기존 주어진 코드 잘 확인하자.


### Try
<!-- 새롭게 시도할 점 -->
객체지향적 설계 방식에 대해 더 고민해보자.
