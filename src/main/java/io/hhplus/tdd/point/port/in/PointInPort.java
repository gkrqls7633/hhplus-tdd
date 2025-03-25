package io.hhplus.tdd.point.port.in;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.UserPoint;

import java.util.List;

public interface PointInPort {
    long getPoint(long id);

    void charge(long id, long amount);

    void use(long id, long amount);

}
