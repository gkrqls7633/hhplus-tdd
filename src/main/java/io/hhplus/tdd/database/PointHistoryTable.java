package io.hhplus.tdd.database;


import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.port.out.PointHistoryOutPort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 해당 Table 클래스는 변경하지 않고 공개된 API 만을 사용해 데이터를 제어합니다.
 */
@Component
public class PointHistoryTable implements PointHistoryOutPort {
    private final List<PointHistory> table = new ArrayList<>();
    private long cursor = 1;

    public List<PointHistory> getTable() {
        return table;
    }

    @Override
    public PointHistory insert(long userId, long amount, TransactionType type, long updateMillis) {
        throttle(300L);
        PointHistory pointHistory = new PointHistory(cursor++, userId, amount, type, updateMillis);
        table.add(pointHistory);

//        System.out.println("Inserted PointHistory: " + pointHistory);

//        System.out.println("--------------전체 이력 -------------");
//        for (PointHistory ph : table) {
//            System.out.println(ph);
//        }

        return pointHistory;
    }

    @Override
    public List<PointHistory> selectAllByUserId(long userId) {
        return table.stream().filter(pointHistory -> pointHistory.userId() == userId).toList();
    }

    private void throttle(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep((long) (Math.random() * millis));
        } catch (InterruptedException ignored) {

        }
    }
}
