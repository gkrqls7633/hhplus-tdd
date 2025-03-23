package io.hhplus.tdd.point.port.in;

import io.hhplus.tdd.point.UserPoint;

public interface PointInPort {
    long getPoint(long id);

    void charge(long id, long amount);
}
