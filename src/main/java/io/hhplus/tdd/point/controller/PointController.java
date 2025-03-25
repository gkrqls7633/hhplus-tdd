package io.hhplus.tdd.point.controller;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.port.in.PointHistoryInPort;
import io.hhplus.tdd.point.port.in.PointInPort;
import io.hhplus.tdd.point.service.PointService;
import io.hhplus.tdd.point.UserPoint;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/point")
@AllArgsConstructor
public class PointController {

    private final PointInPort pointInPort;

    private final PointHistoryInPort pointHistoryInPort;

    private static final Logger log = LoggerFactory.getLogger(PointController.class);

    /**
     * TODO - 특정 유저의 포인트를 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}")
    public long point(@PathVariable long id) {

        long userPoint = pointInPort.getPoint(id);

        return userPoint;
    }

    /**
     * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}/histories")
    public List<PointHistory> history(@PathVariable long id) {
        return pointHistoryInPort.selectAllByUserId(id);
    }

    /**
     * TODO - 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
     * 신규, 기존 유저 구분하여 충전
     * 충전 포인트(amount)가 0보다 커야함
     */
    @PatchMapping("{id}/charge")
    public void charge(@PathVariable long id, @RequestBody long amount){
        pointInPort.charge(id, amount);
    }

    /**
     * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/use")
    public void use(@PathVariable long id, @RequestBody long amount) {
        pointInPort.use(id, amount);
    }
}
