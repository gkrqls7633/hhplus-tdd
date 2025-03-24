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

    @Override
    public void use(long id, long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("사용할 포인트는 0보다 커야합니다.");
        }

        UserPoint userPoint = pointOutPort.getPoint(id);

        //신규 유저의 포인트 사용은 불가 (유효한 유저 id인지 체크)
        if (userPoint == null) {
            throw new IllegalArgumentException("유효하지 않은 유저 ID입니다.");
        }

        //기존 유저의 포인트는 사용할 amount보다 크거나 같아야한다.
        if (userPoint.getPoint() >= amount) {
            pointOutPort.use(userPoint, amount);
        } else {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
    }

}
