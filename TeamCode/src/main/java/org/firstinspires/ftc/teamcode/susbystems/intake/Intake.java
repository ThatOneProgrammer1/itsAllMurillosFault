package org.firstinspires.ftc.teamcode.susbystems.intake;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.susbystems.misc.TelemetryLogger;
import org.firstinspires.ftc.teamcode.susbystems.shooter.Cannon;

import java.util.Arrays;
import java.util.List;

public class Intake {

    private Servo servo1;
    private Servo servo2;
    private Servo servo3;

    List<Lifter> intakeLifters;
    ElapsedTime time;

    public Intake(HardwareMap hardwareMap){
        servo1 = hardwareMap.get(Servo.class, "servo1");
        servo2 = hardwareMap.get(Servo.class, "servo2");
        servo3 = hardwareMap.get(Servo.class, "servo3");

        intakeLifters = Arrays.asList(
                new Lifter(servo1, 0.67, 0.0),
                new Lifter(servo2, 0.5, 0.0),
                new Lifter(servo3, 0.0, 0.5)
        );


        time = new ElapsedTime();

    }


    public void liftBall(Lifter lifter){

        if (!lifter.lifting) {
            lifter.timer.reset();
            lifter.moveServoPos(lifter.getOpenPos());
            lifter.lifting = true;
        }

        if (lifter.lifting && lifter.timer.seconds() > 0.5) {
            lifter.moveServoPos(lifter.getClosePos());
            lifter.lifting = false;
        }
    }

    public void liftRandomBalls(){
       for(Lifter lifter: intakeLifters){
           liftBall(lifter);
       }
    }

    }
