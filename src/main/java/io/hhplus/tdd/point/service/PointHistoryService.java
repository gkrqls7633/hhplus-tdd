package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.port.in.PointHistoryInPort;
import io.hhplus.tdd.point.port.out.PointHistoryOutPort;
import io.hhplus.tdd.point.port.out.PointOutPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PointHistoryService implements PointHistoryInPort {

    private final PointHistoryOutPort pointHistoryOutPort;

    private final PointOutPort pointOutPort;


    @Override
    public List<PointHistory> selectAllByUserId(long id) {

        //유효한 유저 Id 인가?
        UserPoint userPoint = pointOutPort.getPoint(id);
        if (userPoint == null) {
            throw new IllegalArgumentException("유효하지 않은 유저 ID입니다.");
        }

        return pointHistoryOutPort.selectAllByUserId(id);
    }

}
