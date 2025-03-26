package io.hhplus.tdd.point.service.unitTest;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.port.out.PointHistoryOutPort;
import io.hhplus.tdd.point.port.out.PointOutPort;
import io.hhplus.tdd.point.service.PointHistoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest
class PoineHistoryServiceTest {

    @InjectMocks
    private PointHistoryService pointHistoryService;

    @Mock
    private PointHistoryOutPort pointHistoryOutPort;

    @Mock
    private PointOutPort pointOutPort;

    @Test
    @DisplayName("포인트 사용, 충전 이력을 조회할 수 있다.")
    void selectPointHistory() {

        //given
        long userId = 1L;

        PointHistory pointHistory1 = new PointHistory(1, userId, 1000, TransactionType.CHARGE, System.currentTimeMillis());
        PointHistory pointHistory2 = new PointHistory(2, userId, 2000, TransactionType.CHARGE, System.currentTimeMillis());
        PointHistory pointHistory3 = new PointHistory(3, userId, 1000, TransactionType.USE, System.currentTimeMillis());

        List<PointHistory> mockTable = Arrays.asList(pointHistory1, pointHistory2, pointHistory3);

        when(pointHistoryOutPort.selectAllByUserId(userId)).thenReturn(mockTable);
        when(pointOutPort.getPoint(userId)).thenReturn(new UserPoint(userId, 3000, System.currentTimeMillis()));

        //when
        List<PointHistory> pointHistories = pointHistoryService.selectAllByUserId(userId);

        //then
        System.out.println("--------------전체 이력 -------------");
        for (PointHistory ph : pointHistories) {
            System.out.println(ph);
        }
        verify(pointHistoryOutPort, times(1)).selectAllByUserId(userId);
    }

    @Test
    @DisplayName("유효하지 않은 유저Id의 포인트 사용, 충전 이력을 조회할 수 없다.")
    void notSelectPointHistoryWithNoExistsUser() {

        //given
        long userId = 4L;

        PointHistory pointHistory1 = new PointHistory(1, userId, 1000, TransactionType.CHARGE, System.currentTimeMillis());
        PointHistory pointHistory2 = new PointHistory(2, userId, 2000, TransactionType.CHARGE, System.currentTimeMillis());
        PointHistory pointHistory3 = new PointHistory(3, userId, 1000, TransactionType.USE, System.currentTimeMillis());

        List<PointHistory> mockTable = Arrays.asList(pointHistory1, pointHistory2, pointHistory3);

        when(pointHistoryOutPort.selectAllByUserId(userId)).thenReturn(mockTable);

        //when & then
        assertThatThrownBy(() -> pointHistoryService.selectAllByUserId(userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효하지 않은 유저 ID입니다.");
    }
}