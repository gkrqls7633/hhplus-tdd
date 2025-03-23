package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.port.out.PointOutPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PointServiceTest {

    @InjectMocks
    private PointService pointService;  // PointService 객체에 Mock된 pointOutPort를 주입

    @Mock
    private PointOutPort pointOutPort;  // PointOutPort를 Mock 객체로 생성

    @Test
    @DisplayName("특정 유저의 포인트는 0보다 크거나 같아야 한다.")
    void getNonNegativePoint() {

        // given
        // Mock 처리 : 유저 1의 포인트 1000 반환
        long userId = 1L;
        when(pointOutPort.getPoint(userId)).thenReturn(new UserPoint(userId, 1000, System.currentTimeMillis()));

        // when
        long point = pointService.getPoint(1L);

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
}