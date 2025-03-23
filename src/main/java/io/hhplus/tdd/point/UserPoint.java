package io.hhplus.tdd.point;

public record UserPoint(
        long id,
        long point,
        long updateMillis
) {

    public static UserPoint empty(long id) {
        return new UserPoint(id, 0, System.currentTimeMillis());
    }

    // UserPoint 객체를 0보다 큰 포인트로 생성할 수 있도록 유효성 추가
    public static UserPoint of(long id, long point) {
        if (point < 0) {
            throw new IllegalArgumentException("포인트는 0 이상이어야 합니다.");
        }
        return new UserPoint(id, point, System.currentTimeMillis());
    }

    public Long getPoint() {
        return point;
    }
}
