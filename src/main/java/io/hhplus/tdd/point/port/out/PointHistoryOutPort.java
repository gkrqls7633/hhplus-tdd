package io.hhplus.tdd.point.port.out;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;

import java.util.List;

public interface PointHistoryOutPort {

    List<PointHistory> selectAllByUserId(long id);

    PointHistory insert(long id, long amount, TransactionType transactionType, long l);
}
