package io.hhplus.tdd.point.service.integrationTest;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.PointTempInmemory;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
