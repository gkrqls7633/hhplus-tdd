package io.hhplus.tdd.point.port.in;

import io.hhplus.tdd.point.PointHistory;

import java.util.List;

public interface PointHistoryInPort {

    List<PointHistory> selectAllByUserId(long id);
}
