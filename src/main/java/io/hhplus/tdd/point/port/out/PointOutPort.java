package io.hhplus.tdd.point.port.out;

import io.hhplus.tdd.point.UserPoint;

public interface PointOutPort {

    UserPoint getPoint(long id);

    void charge(UserPoint userPoint, long amount);

    void use(UserPoint userPoint, long amount);
}
