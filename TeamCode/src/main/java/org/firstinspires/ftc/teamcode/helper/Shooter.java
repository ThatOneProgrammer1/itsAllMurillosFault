package org.firstinspires.ftc.teamcode.helper;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Shooter {

    DcMotorEx shooter1;
    DcMotorEx shooter2;
    Servo shooterServo;

    double basePower = 0.0;
    double servoPos = 0.0;
    final double deadzone = 250;

    public Shooter(HardwareMap hardwareMap){

        shooter1 = hardwareMap.get(DcMotorEx.class, "shooter1");
        shooter1.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        shooter2 = hardwareMap.get(DcMotorEx.class, "shooter2");
        shooter2.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        shooterServo = hardwareMap.get(Servo.class, "servo1");

    }

    double shootPower = 0.0;

    public double scalePower(double distance){

        if(distance != 0){
            double power = Range.scale(distance, 30, 115, 0.475, 0.625);
            shootPower = Range.clip(power, .475, .625);
            return shootPower;
        }

        return shootPower;

    }

    public double scaleServoPos(double distance){

        if(distance != 0){
            double power = Range.scale(distance, 30, 115, .1, .6);
            servoPos = Range.clip(power, .1, .6);
            return servoPos;
        }

        return servoPos;
    }

    public void logServo(Telemetry telemetry){
        telemetry.addData("Servo Pos", servoPos);
        telemetry.addData("Servo Angle", shooterServo.getPosition());
    }

    public double shoot(double distance){

        basePower = scalePower(distance);
        servoPos = scaleServoPos(distance);

        shooter1.setPower(basePower);
        shooter2.setPower(basePower);

        shooterServo.setPosition(servoPos);

        return basePower;
    }


    public void setServo(double pos){
        servoPos = pos;
        shooterServo.setPosition(pos);
    }




    public void stopShooter(){
        shooter1.setPower(0);
        shooter2.setPower(0);
    }

    public boolean isShooterReady(){

        double expectedVelocity = 6000 * basePower;
        double velocity = (shooter1.getVelocity() +  shooter2.getVelocity()) / 2;

        return velocity > expectedVelocity - deadzone;


    }

}
