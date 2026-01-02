package org.firstinspires.ftc.teamcode.subsystems.indexer;


import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.other.TelemetryLogger;

public class indexing {

    private Servo sorter1;
    private Servo sorter2;
    private Servo sorter3;

    private static final double SHOOT_POSITION1 = 0.85;
    private static final double REST_POSITION1 = 0.125;
    private static final double SHOOT_POSITION2 = 0.05;
    private static final double REST_POSITION2 = 0.54;
    private static final double SHOOT_POSITION3 = 0.667;
    private static final double REST_POSITION3 = 0.225;


    private static final double SHOOT_TIME_S = 0.6;

    private enum IndexState {
        Idle,
        ShootingFirst,
        ShootingSecond,
        ShootingThird
    }
    private Timer timer = new Timer();
    private IndexState state = IndexState.Idle;

    public void startShooting() {
        state = IndexState.ShootingFirst;
        timer.resetTimer();
    }

    public void init(HardwareMap hwMap, TelemetryLogger telemetryLogger){
        sorter3 = hwMap.get(Servo.class, "sorter1");
        sorter1 = hwMap.get(Servo.class, "sorter2");
        sorter2 = hwMap.get(Servo.class, "sorter3");
        state = IndexState.Idle;
        loggah = telemetryLogger;

    }
    private TelemetryLogger loggah;

    /**
     * Call in loop
     */
    public void update() {
//        loggah.logState(state.toString());
        switch (state) {
            case Idle:
                sorter1.setPosition(REST_POSITION1);
                sorter2.setPosition(REST_POSITION2);
                sorter3.setPosition(REST_POSITION3);
                break;
            case ShootingFirst:
                sorter1.setPosition(SHOOT_POSITION1);
                sorter2.setPosition(REST_POSITION2);
                sorter3.setPosition(REST_POSITION3);
                if (timer.getElapsedTimeSeconds() > SHOOT_TIME_S) {
                    timer.resetTimer();
                    state = IndexState.ShootingSecond;
                }
                break;
            case ShootingSecond:
                sorter1.setPosition(REST_POSITION1);
                sorter2.setPosition(SHOOT_POSITION2);
                sorter3.setPosition(REST_POSITION3);
                if (timer.getElapsedTimeSeconds() > SHOOT_TIME_S){
                    timer.resetTimer();
                    state = IndexState.ShootingThird;
                }
                break;
            case ShootingThird:
                sorter1.setPosition(REST_POSITION1);
                sorter2.setPosition(REST_POSITION2);
                sorter3.setPosition(SHOOT_POSITION3);
                if (timer.getElapsedTimeSeconds() > SHOOT_TIME_S){
                    timer.resetTimer();
                    state = IndexState.Idle;
                }
                break;
        }
    }

}
