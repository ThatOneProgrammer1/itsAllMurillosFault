package org.firstinspires.ftc.teamcode.susbystems.indexing;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.susbystems.misc.TelemetryLogger;

public class Indexer {

    private Servo servo1;
    private Servo servo2;
    private Servo servo3;

    private Sorter sorter1;
    private Sorter sorter2;
    private Sorter sorter3;

    private final double SHOOT_TIME_S = 0.25;

    public Indexer(HardwareMap hardwareMap, TelemetryLogger telemetryLogger){
        servo1 = hardwareMap.get(Servo.class, "servo1");
        servo2 = hardwareMap.get(Servo.class, "servo2");
        servo3 = hardwareMap.get(Servo.class, "servo3");

        sorter1 = new Sorter(servo1, 0.67, 0.0);
        sorter2 = new Sorter(servo2, 0.0, 1.0);
        sorter3 = new Sorter(servo3, 0.0, 1.0);

        loggah = telemetryLogger;

    }


    private enum IndexState {
        Idle,
        ShootingFirst,
        ShootingSecond,
        ShootingThird
    }
    private com.pedropathing.util.Timer timer = new com.pedropathing.util.Timer();
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
                sorter1.setPosition(sorter1.getRestPosition());
                sorter2.setPosition(sorter2.getRestPosition());
                sorter3.setPosition(sorter3.getRestPosition());
                break;
            case ShootingFirst:
                sorter1.setPosition(sorter1.getShootPosition());
                sorter2.setPosition(sorter2.getRestPosition());
                sorter3.setPosition(sorter3.getShootPosition());
                if (timer.getElapsedTimeSeconds() > SHOOT_TIME_S) {
                    timer.resetTimer();
                    state = IndexState.ShootingSecond;
                }
                break;
            case ShootingSecond:
                sorter1.setPosition(sorter1.getRestPosition());
                sorter2.setPosition(sorter2.getShootPosition());
                sorter3.setPosition(sorter3.getRestPosition());
                if (timer.getElapsedTimeSeconds() > SHOOT_TIME_S){
                    timer.resetTimer();
                    state = IndexState.ShootingThird;
                }
                break;
            case ShootingThird:
                sorter1.setPosition(sorter1.getRestPosition());
                sorter2.setPosition(sorter2.getRestPosition());
                sorter3.setPosition(sorter3.getShootPosition());
                if (timer.getElapsedTimeSeconds() > SHOOT_TIME_S){
                    timer.resetTimer();
                    state = IndexState.Idle;
                }
                break;
        }
    }

    }
