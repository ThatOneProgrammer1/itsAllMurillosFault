package org.firstinspires.ftc.teamcode.subsystems;


import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.helper.TelemetryLogger;

public class indexing {

    private Servo sorter1;
    private Servo sorter2;
    private Servo sorter3;

    private static final double SHOOT_POSITION = 1.0;
    private static final double REST_POSITION = 0.0;
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
                if (timer.getElapsedTimeSeconds() > SHOOT_TIME_S) {
                    timer.resetTimer();
                    state = IndexState.ShootingSecond;
                }
                break;
            case ShootingSecond:
                sorter2.setPosition(SHOOT_POSITION);
                if (timer.getElapsedTimeSeconds() > SHOOT_TIME_S){
                    timer.resetTimer();
                    state = IndexState.ShootingThird;
                }
                break;
            case ShootingThird:
                sorter3.setPosition(SHOOT_POSITION);
                if (timer.getElapsedTimeSeconds() > SHOOT_TIME_S){
                    timer.resetTimer();
                    state = IndexState.Idle;
                }
                break;
        }
    }

}
