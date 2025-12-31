package org.firstinspires.ftc.teamcode.subsystems.indexer;


import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.other.TelemetryLogger;

public class indexing {

    private Servo sorter1;
    private Servo sorter2;
    private Servo sorter3;

    private static final double SHOOT_POSITION1 = 0.667;
    private static final double REST_POSITION1 = 0.0;
    private static final double SHOOT_POSITION2 = 1.0;
    private static final double REST_POSITION2 = 0.0;
    private static final double SHOOT_POSITION3 = 1.0;
    private static final double REST_POSITION3 = 0.0;

    private static final double SHOOT_TIME_S = 0.25;

    private enum IndexState {
        Idle,
        ShootingFirst,
        ShootingSecond,
        ShootingThird
    }
    private Timer timer = new Timer();
    private IndexState state;

    public void startShooting() {
        state = IndexState.ShootingFirst;
        timer.resetTimer();
    }

    private TelemetryLogger loggah;

    /**
     * Call in loop
     */
    public void update() {
        loggah.logState(state.toString());
        switch (state) {
            case Idle:
                sorter1.setPosition(REST_POSITION);
                sorter2.setPosition(REST_POSITION);
                sorter3.setPosition(REST_POSITION);
                break;
            case ShootingFirst:
                sorter1.setPosition(SHOOT_POSITION);
                sorter2.setPosition(REST_POSITION);
                sorter3.setPosition(REST_POSITION);
                if (timer.getElapsedTimeSeconds() > SHOOT_TIME_S) {
                    timer.resetTimer();
                    state = IndexState.ShootingSecond;
                }
                break;
            case ShootingSecond:
                sorter1.setPosition(REST_POSITION);
                sorter2.setPosition(SHOOT_POSITION);
                sorter3.setPosition(REST_POSITION);
                if (timer.getElapsedTimeSeconds() > SHOOT_TIME_S){
                    timer.resetTimer();
                    state = IndexState.ShootingThird;
                }
                break;
            case ShootingThird:
                sorter1.setPosition(REST_POSITION);
                sorter2.setPosition(REST_POSITION);
                sorter3.setPosition(SHOOT_POSITION);
                if (timer.getElapsedTimeSeconds() > SHOOT_TIME_S){
                    timer.resetTimer();
                    state = IndexState.Idle;
                }
                break;
        }
    }

}
