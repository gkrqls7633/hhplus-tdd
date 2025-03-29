package io.hhplus.tdd.point.port.out;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.UserPoint;

import java.util.List;

public interface PointOutPort {

    UserPoint getPoint(long id);

    UserPoint charge(UserPoint userPoint, long amount);

    UserPoint use(UserPoint userPoint, long amount);

}
