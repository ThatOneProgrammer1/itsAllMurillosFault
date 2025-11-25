package org.firstinspires.ftc.teamcode.helper;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Shooter {

    DcMotorEx shooter1;
    DcMotorEx shooter2;

    Servo shooterServo;

    public Shooter(HardwareMap hardwareMap){

        shooter1 = hardwareMap.get(DcMotorEx.class, "shooter1");
        shooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        shooter2 = hardwareMap.get(DcMotorEx.class, "shooter2");
        shooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        shooterServo = hardwareMap.get(Servo.class, "servo1");

    }

    double shootPower = 0.0;

    public double testShootDistanceScale(double distance){

        if(distance != 0){
            double power = Range.scale(distance, 30, 115, 0.475, 0.625);
            shootPower = Range.clip(power, .475, .625);
            return shootPower;
        }
        return shootPower;

    }

    public double shoot(double distance){
        double power = testShootDistanceScale(distance);

        shooter1.setPower(power);
        shooter2.setPower(power);

        return power;
    }



    public void stopShooter(){
        shooter1.setPower(0);
        shooter2.setPower(0);
    }

}
