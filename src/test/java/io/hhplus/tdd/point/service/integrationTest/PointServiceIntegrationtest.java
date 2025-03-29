package io.hhplus.tdd.point.service.integrationTest;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.PointTempInmemory;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.port.out.PointHistoryOutPort;
import io.hhplus.tdd.point.port.out.PointOutPort;
import io.hhplus.tdd.point.service.PointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PointServiceIntegrationtest {

    private static final Logger log = LoggerFactory.getLogger(PointServiceIntegrationtest.class);

    private static final long USER_ID = 1L;

    @Autowired
    private PointOutPort pointOutPort;

    @Autowired
    private PointService pointService;

    @Autowired
    private PointHistoryOutPort pointHistoryOutPort;

    @BeforeEach
    void setUp() {

        //pointTempInmemory 초기화
        PointTempInmemory pointTempInmemory = (PointTempInmemory) pointOutPort;
        pointTempInmemory.getDbMap().clear();

        pointTempInmemory.getDbMap().put(1L, UserPoint.of(1L, 1000));
        pointTempInmemory.getDbMap().put(2L, UserPoint.of(2L, 500));
        pointTempInmemory.getDbMap().put(3L, UserPoint.of(3L, 0));

        //PointHistoryTable 초기화
        PointHistoryTable pointHistoryTable = (PointHistoryTable) pointHistoryOutPort;
        pointHistoryTable.getTable().clear(); // table 초기화

    }


    @Test
    @DisplayName("포인트 충전, 사용, 조회, 이력 조회 통합테스트")
    void pointserviceIntegrationTest() {

        /*
        1. 유저 정보 확인 및 유효한지 체크
        2. 포인트 충전
        3. 포인트 사용
        4. 전체 포인트 이력 조회
        */

        //유저 정보 확인
        UserPoint userPoint = pointOutPort.getPoint(USER_ID);
        assertEquals(1000L, userPoint.getPoint());

        //충전 기능 검증
        UserPoint chUserPoint = pointService.charge(USER_ID, 1000L);
        log.info("chUserPoint: {}", chUserPoint);
        assertEquals(2000L, chUserPoint.getPoint());

        //충전 완료 후 history insert 내역 확인
        List<PointHistory> historyAfterCharge =  pointHistoryOutPort.selectAllByUserId(USER_ID);
        log.info("history1: {}", historyAfterCharge);
        assertEquals(1, historyAfterCharge.size());

        //사용 기능 검증
        UserPoint useUserPoint =  pointService.use(USER_ID, 2000L);
        log.info("useUserPoint: {}", useUserPoint);
        assertEquals(0L, useUserPoint.getPoint());

        //사용 완료 후 history insert 내역 확인
        List<PointHistory> historyAfterUse = pointHistoryOutPort.selectAllByUserId(USER_ID);
        log.info("historyAfterUse: {}", historyAfterUse);
        assertEquals(2, historyAfterUse.size());
    }

    @Test
    @DisplayName("스레드의 동시성 이슈 처리를 통해 포인트 사용을 검증한다.")
    void usePointWithSyncronized() throws InterruptedException {
        //given
        long userId = 1L;
        final long amount = 200;  // 각 스레드가 200원씩 사용
        UserPoint userPoint = pointOutPort.getPoint(userId);
        log.info("userPoint: {}", userPoint);

        Thread thread1 = new Thread(() -> {
            System.out.println("Thread 1 Starting");
            UserPoint useUserPoint1 =  pointService.use(USER_ID, amount);
            System.out.println("Thread 1 Finished");
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("Thread 2 Starting");
            UserPoint useUserPoint2 =  pointService.use(USER_ID, amount);
            System.out.println("Thread 2 Finished");
        });

        Thread thread3 = new Thread(() -> {
            System.out.println("Thread 3 Starting");
            UserPoint useUserPoint3 =  pointService.use(USER_ID, amount);
            System.out.println("Thread 3 Finished");
        });

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        assertEquals(400L, pointOutPort.getPoint(userId).getPoint());
    }

    @Test
    @DisplayName("스레드의 동시성 이슈 처리를 통해 포인트 충전을 검증한다.")
    void chargePointWithSyncronized() throws InterruptedException {
        //given
        long userId = 1L;
        final long amount = 200;  // 각 스레드가 200원씩 충전
        UserPoint userPoint = pointOutPort.getPoint(userId);
        log.info("userPoint: {}", userPoint);

        Thread thread1 = new Thread(() -> {
            System.out.println("Thread 1 Starting");
            UserPoint useUserPoint1 =  pointService.charge(USER_ID, amount);
            System.out.println("Thread 1 Finished");
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("Thread 2 Starting");
            UserPoint useUserPoint2 =  pointService.charge(USER_ID, amount);
            System.out.println("Thread 2 Finished");
        });

        Thread thread3 = new Thread(() -> {
            System.out.println("Thread 3 Starting");
            UserPoint useUserPoint3 =  pointService.charge(USER_ID, amount);
            System.out.println("Thread 3 Finished");
        });

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        assertEquals(1600L, pointOutPort.getPoint(userId).getPoint());
    }

    @Test
    @DisplayName("스레드의 동시성 이슈 처리를 통해 포인트 사용과 충전을 검증한다.")
    void useAndChargePointWithSyncronized() throws InterruptedException {
        //given
        long userId = 1L;
        final long amount = 200;  // 각 스레드가 200원씩 사용/충전
        UserPoint userPoint = pointOutPort.getPoint(userId);
        log.info("userPoint: {}", userPoint);

        Thread thread1 = new Thread(() -> {
            System.out.println("Thread 1 Starting");
            UserPoint useUserPoint1 =  pointService.use(USER_ID, amount);
            System.out.println("Thread 1 Finished");
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("Thread 2 Starting");
            UserPoint useUserPoint2 =  pointService.charge(USER_ID, amount);
            System.out.println("Thread 2 Finished");
        });

        Thread thread3 = new Thread(() -> {
            System.out.println("Thread 3 Starting");
            UserPoint useUserPoint3 =  pointService.charge(USER_ID, amount);
            System.out.println("Thread 3 Finished");
        });

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        assertEquals(1200L, pointOutPort.getPoint(userId).getPoint());
    }
}
