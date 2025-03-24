package io.hhplus.tdd.point;

import io.hhplus.tdd.point.port.out.PointOutPort;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PointTempInmemory implements PointOutPort {

    private static final Logger log = LoggerFactory.getLogger(PointTempInmemory.class);

    // 가상 db 역할
    private Map<Long, UserPoint> dbMap = new HashMap<>();

    public PointTempInmemory() {
        dbMap.put(1L, UserPoint.of(1L, 1000));
        dbMap.put(2L, UserPoint.of(2L, 500));
        dbMap.put(3L, UserPoint.of(3L, 0));
    }

    @Override
    public UserPoint getPoint(long id) {
        return dbMap.get(id);
    }

    @Override
    public void charge(UserPoint userPoint, long amount) {
        UserPoint newUserPoint = userPoint.chargePoint(userPoint.id(), amount);
        dbMap.put(userPoint.id(), newUserPoint);

        log.info("Updated dbMap: {}", dbMap);
    }

    @Override
    public void use(UserPoint userPoint, long amount) {
        UserPoint newUserPoint = userPoint.usePoint(userPoint.id(), amount);
        dbMap.put(userPoint.id(), newUserPoint);

        log.info("Updated dbMap: {}", dbMap);

    }
}
