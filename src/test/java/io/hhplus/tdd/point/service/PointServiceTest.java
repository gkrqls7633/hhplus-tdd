package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.PointTempInmemory;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.port.out.PointHistoryOutPort;
import io.hhplus.tdd.point.port.out.PointOutPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PointServiceTest {

    private static final Logger log = LoggerFactory.getLogger(PointTempInmemory.class);

    @InjectMocks
    private PointService pointService;  // PointService 객체에 Mock된 pointOutPort를 주입

    @Mock
    private PointOutPort pointOutPort;  // PointOutPort를 Mock 객체로 생성

    @Mock
    private PointHistoryOutPort pointHistoryOutPort;  // PointOutPort를 Mock 객체로 생성


    @Test
    @DisplayName("특정 유저의 포인트는 0보다 크거나 같아야 한다.")
    void getNonNegativePoint() {

        // given
        // Mock 처리 : 유저 1의 포인트 1000 반환
        long userId = 1L;
        when(pointOutPort.getPoint(userId)).thenReturn(new UserPoint(userId, 1000, System.currentTimeMillis()));

        // when
        long point = pointService.getPoint(userId);

        //then
        assertTrue(point >= 0);
        verify(pointOutPort, times(1)).getPoint(userId);
    }

    @Test
    @DisplayName("특정 유저의 포인트는 0일 수 있다.")
    void getZeroPoint() {

        // given
        // Mock 처리 : 유저 1의 포인트 0 반환
        long userId = 1L;
        when(pointOutPort.getPoint(userId)).thenReturn(new UserPoint(userId, 0, System.currentTimeMillis()));

        // when
        long point = pointService.getPoint(userId);

        // then
        assertEquals(0L, point);
        verify(pointOutPort, times(1)).getPoint(userId);
    }

    @Test
    @DisplayName("존재하지 않는 유저의 포인트를 조회하면 에러 반환한다.")
    void getPointExistsUser() {

        //given
        long userId = 4L;

        //when & then
        assertThatThrownBy(() -> pointService.getPoint(userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효하지 않은 유저 ID입니다.");

    }

    @Test
    @DisplayName("기존 유저의 포인트를 충전할 수 있다.")
    void chargePointExistsUser() {

        //given
        long userId = 1L;
        long amount = 1000L;

        //최초 1000포인트 존재하도록 mock 처리
        UserPoint mockUserPoint = new UserPoint(userId, 1000, System.currentTimeMillis());
        when(pointOutPort.getPoint(userId)).thenReturn(mockUserPoint);

        log.info("before point : {} ", mockUserPoint.point());

        // pointOutPort.charge(mockUserPoint, amount)가 호출될 때 포인트 값을 갱신하도록 설정
        // void의 경우 doAnswer를 통해 처리 동작 셋팅
        doAnswer(invocation -> {
            UserPoint updatedUserPoint = invocation.getArgument(0);
            long newPoint = updatedUserPoint.getPoint() + amount;
            updatedUserPoint = new UserPoint(updatedUserPoint.id(), newPoint, System.currentTimeMillis());

            // 포인트가 갱신된 UserPoint 객체를 pointOutPort에 반영
            when(pointOutPort.getPoint(userId)).thenReturn(updatedUserPoint);

            return null;  // void 메서드는 null 반환
        }).when(pointOutPort).charge(any(UserPoint.class), eq(amount));

        //when
        pointService.charge(userId, amount);
        long point = pointService.getPoint(userId);

        log.info("after point : {} ", point);

        // then
        assertEquals(2000, point);
    }

    @Test
    @DisplayName("신규 유저의 포인트를 충전할 수 있다.")
    void chargePointNewUser() {

        //given
        long userId = 4L;
        long amount = 1000L;

        //기존 유저 존재하지 않도록 null 반환하도록 mock 처리
        UserPoint mockUserPoint = UserPoint.empty(userId);
        when(pointOutPort.getPoint(userId)).thenReturn(null);

        log.info("before point : {} ", mockUserPoint.point());


        // pointOutPort.charge(mockUserPoint, amount)가 호출될 때 포인트 값을 갱신하도록 설정
        // void의 경우 doAnswer를 통해 처리 동작 셋팅
        doAnswer(invocation -> {
            UserPoint updatedUserPoint = invocation.getArgument(0);
            long newPoint = updatedUserPoint.getPoint() + amount;
            updatedUserPoint = new UserPoint(updatedUserPoint.id(), newPoint, System.currentTimeMillis());

            // 포인트가 갱신된 UserPoint 객체를 pointOutPort에 반영
            when(pointOutPort.getPoint(userId)).thenReturn(updatedUserPoint);

            return null;  // void 메서드는 null 반환
        }).when(pointOutPort).charge(any(UserPoint.class), eq(amount));

        //when
        pointService.charge(userId, amount);
        long point = pointService.getPoint(userId);

        log.info("after point : {} ", point);

        // then
        assertEquals(1000, point);
    }

    @Test
    @DisplayName("포인트 충전은 0보다 커야한다.")
    void chargePointNonNegativeAmount() {

        long userId = 1L;
        long amount = 0L;
        when(pointOutPort.getPoint(userId)).thenReturn(new UserPoint(userId, 1000, System.currentTimeMillis()));

        //when & then
        assertThatThrownBy(() -> pointService.charge(userId, amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("충전할 포인트는 0보다 커야합니다.");

    }

    @Test
    @DisplayName("유효하지 않은 유저Id는 포인트를 사용할 수 없다.")
    void notUsePointWithNoExistsUser() {
        //given
        long userId = 4L;
        long amount = 1000L;

        //기존 유저 존재하지 않도록 null 반환하도록 mock 처리
        when(pointOutPort.getPoint(userId)).thenReturn(null);

        //when & then
        assertThatThrownBy(() -> pointService.use(userId, amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효하지 않은 유저 ID입니다.");
    }

    @Test
    @DisplayName("기존 포인트보다 많은 양의 포인트를 사용할 수 없다.")
    void notUsePointWithNotEnoughPoint() {
        //given
        long userId = 1L;
        long amount = 1000L;

        UserPoint mockUserPoint = new UserPoint(userId, 500, System.currentTimeMillis());

        when(pointOutPort.getPoint(userId)).thenReturn(mockUserPoint);

        //when & then
        assertThatThrownBy(() -> pointService.use(userId, amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("포인트가 부족합니다.");
    }

    @Test
    @DisplayName("기존 유저의 포인트를 사용할 수 있다.")
    void usePointExistsUser() {

        //given
        long userId = 1L;
        long amount = 1000L;

        UserPoint mockUserPoint = new UserPoint(userId, 2000, System.currentTimeMillis());

        when(pointOutPort.getPoint(userId)).thenReturn(mockUserPoint);

        // pointOutPort.use(id, amount)가 호출될 때 포인트 값을 갱신하도록 설정
        // void의 경우 doAnswer를 통해 처리 동작 셋팅
        doAnswer(invocation -> {
            UserPoint updatedUserPoint = invocation.getArgument(0);
            long newPoint = updatedUserPoint.getPoint() - amount;
            updatedUserPoint = new UserPoint(userId, newPoint, System.currentTimeMillis());

            // 포인트가 갱신된 UserPoint 객체를 pointOutPort에 반영
            when(pointOutPort.getPoint(userId)).thenReturn(updatedUserPoint);

            return null;
        }).when(pointOutPort).use(any(UserPoint.class), eq(amount));

        //when
        pointService.use(userId, amount);
        long point = pointService.getPoint(userId);

        //then
        assertEquals(1000, point);
        verify(pointOutPort, times(1)).use(mockUserPoint, amount);
    }
}