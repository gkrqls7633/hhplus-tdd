package io.hhplus.tdd.point;

import io.hhplus.tdd.point.port.out.PointOutPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * UserPointTableTable 클래스를 사용했어야 했는데... 대신 PointTempInmemory 이걸로 대체.
 */
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

    public Map<Long, UserPoint> getDbMap() {
        return dbMap;
    }

    @Override
    public UserPoint getPoint(long id) {
        throttle(300);
        return dbMap.get(id);
    }

    @Override
    public UserPoint charge(UserPoint userPoint, long amount) {
        throttle(300);
        UserPoint cgUserPoint = userPoint.chargePoint(userPoint.id(), amount);
        dbMap.put(userPoint.id(), cgUserPoint);

        log.info("Updated dbMap: {}", dbMap);

        return cgUserPoint;
    }

    @Override
    public UserPoint use(UserPoint userPoint, long amount) {
        throttle(300);
        UserPoint useUserPoint = userPoint.usePoint(userPoint.id(), amount);
        dbMap.put(userPoint.id(), useUserPoint);

        log.info("Updated dbMap: {}", dbMap);

        return useUserPoint;
    }

    private void throttle(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep((long) (Math.random() * millis));
        } catch (InterruptedException ignored) {

        }
    }
}
