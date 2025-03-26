package io.hhplus.tdd.point.service.integrationTest;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.port.out.PointHistoryOutPort;
import io.hhplus.tdd.point.port.out.PointOutPort;
import io.hhplus.tdd.point.service.PointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

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
        UserPoint useUserPoint =  pointService.use(USER_ID, 1000L);
        log.info("useUserPoint: {}", useUserPoint);
        assertEquals(1000L, useUserPoint.getPoint());

        //사용 완료 후 history insert 내역 확인
        List<PointHistory> historyAfterUse = pointHistoryOutPort.selectAllByUserId(USER_ID);
        log.info("historyAfterUse: {}", historyAfterUse);
        assertEquals(2, historyAfterUse.size());
    }

    @Test
    @DisplayName("포인트 사용을 동시에 여러 자원에서 접근한다.")
    void usePointConcurrently() throws InterruptedException {

        //given
        long userId = 1L;
        final long amount = 200;  // 각 스레드가 200원씩 사용
        UserPoint userPoint = pointOutPort.getPoint(userId);

        log.info("userPoint: {}", userPoint);

        ExecutorService executor = Executors.newFixedThreadPool(5);

        // 5개의 스레드가 동시에 포인트를 사용하려고 시도
        AtomicInteger count = new AtomicInteger();
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                try {
                    pointOutPort.use(userPoint, amount);
                    count.getAndIncrement();

                    log.info("count : {} ", count.get());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        log.info("count : {} ", count.get());

        // 종료 대기
        executor.shutdown();
//        executor.awaitTermination(1, TimeUnit.MINUTES);  // 1분 이내로 종료되기를 기다림

        // then
        // 5번 사용하려고 했으므로, 최종 포인트는 0이 되어야 한다 (1000 - 200 * 5)
        UserPoint finalUserPoint = pointOutPort.getPoint(userId);
        assertNotNull(finalUserPoint);
        assertEquals(0L, finalUserPoint.getPoint());  // 최종 포인트는 0이어야 함
    }

}
