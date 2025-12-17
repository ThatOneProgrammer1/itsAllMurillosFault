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

    List<Servo> intakeServos;
    ElapsedTime time;

    public Intake(HardwareMap hardwareMap){
        servo1 = hardwareMap.get(Servo.class, "servo1");
        servo2 = hardwareMap.get(Servo.class, "servo2");
        servo3 = hardwareMap.get(Servo.class, "servo3");
        intakeServos = Arrays.asList(
                servo1, servo2, servo3
        );
        time = new ElapsedTime();

    }

    boolean lifting = false;

    public void liftBall(int servoIndex){
        Servo servo = intakeServos.get(servoIndex);

        if (!lifting) {
            time.reset();
            servo.setPosition(0.7);
            lifting = true;
        }

        if (lifting && time.seconds() > 1) {
            servo.setPosition(0);
            lifting = false;
        }
    }

    }
