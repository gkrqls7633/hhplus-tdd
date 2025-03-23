package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.port.in.PointInPort;
import io.hhplus.tdd.point.port.out.PointOutPort;
import org.springframework.stereotype.Service;

@Service
public class PointService implements PointInPort {

    private PointOutPort pointOutPort;

    public PointService(PointOutPort pointOutPort) {
        this.pointOutPort = pointOutPort;
    }

    public long getPoint(long id) {

        UserPoint userPoint = pointOutPort.getPoint(id);

        if (userPoint == null) {
            throw new IllegalArgumentException("유효하지 않은 유저 ID입니다.");
        }

        return userPoint.getPoint();
    }

}
