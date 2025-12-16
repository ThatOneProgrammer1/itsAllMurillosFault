package org.firstinspires.ftc.teamcode.susbystems.intake;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.susbystems.misc.TelemetryLogger;
import org.firstinspires.ftc.teamcode.susbystems.shooter.Cannon;

public class Intake {
    private Servo servo1;

    public Intake(HardwareMap hardwareMap){
        servo1 = hardwareMap.get(Servo.class, "servo1");
    }

    public void liftBall(Cannon cannon, boolean intakeActive, TelemetryLogger telemetryLogger){
        if(cannon.isShooterReady() && intakeActive){
            servo1.setPosition(1);
        }
        else{
            servo1.setPosition(0);
        }
    }

    }
