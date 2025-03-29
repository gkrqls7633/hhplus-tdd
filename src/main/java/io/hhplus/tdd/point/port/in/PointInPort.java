package io.hhplus.tdd.point.port.in;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.UserPoint;

import java.util.List;

public interface PointInPort {
    long getPoint(long id);

    UserPoint charge(long id, long amount);

    UserPoint use(long id, long amount);

}
