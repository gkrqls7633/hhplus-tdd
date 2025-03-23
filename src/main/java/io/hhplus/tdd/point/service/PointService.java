package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.port.in.PointInPort;
import io.hhplus.tdd.point.port.out.PointOutPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PointService implements PointInPort {

    private final PointOutPort pointOutPort;

    /* 특정 유저 포인트 조회 */
    @Override
    public long getPoint(long id) {

        UserPoint userPoint = pointOutPort.getPoint(id);

        if (userPoint == null) {
            throw new IllegalArgumentException("유효하지 않은 유저 ID입니다.");
        }

        return userPoint.getPoint();
    }

    /* 특정 유저 포인트 충전 */
    @Override
    public void charge(long id, long amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("충전할 포인트는 0보다 커야합니다.");
        }
        UserPoint userPoint = pointOutPort.getPoint(id);

        //신규 유저의 포인트 충전
        if (userPoint == null) {
            UserPoint newUserPoint = UserPoint.of(id, 0);
            pointOutPort.charge(newUserPoint, amount);

        //기존에 존재하는 유저의 포인트 충전
        } else{
            pointOutPort.charge(userPoint, amount);
        }
    }

}
