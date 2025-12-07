package org.firstinspires.ftc.teamcode.susbystems.shooter;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.susbystems.misc.TelemetryLogger;

public class Cannon  {

    private DcMotorEx shooter1;
    private DcMotorEx shooter2;
    private Servo shooterServo;


    private double basePower = 0.0;
    private double servoPos = 0.0;
    private double shootPower = 0.0;

    // constants
    private final double DEADZONE = 250;
    private final double BASE_VELOCITY = 6000;
    private final double MAX_POWER = 0.625;
    private final double MIN_POWER = 0.475;
    private final double MIN_SERVO_POS = 0.1;
    private final double MAX_SERVO_POS = 0.6;
    private final double MIN_DISTANCE = 30;
    private final double MAX_DISTANCE = 115;

    public Cannon(HardwareMap hardwareMap){

        shooter1 = hardwareMap.get(DcMotorEx.class, "shooter1");
        shooter1.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        shooter2 = hardwareMap.get(DcMotorEx.class, "shooter2");
        shooter2.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        shooterServo = hardwareMap.get(Servo.class, "servo1");

    }

    public double scalePower(double distance){

        if(distance != 0){
            double power = Range.scale(distance, MIN_DISTANCE, MAX_DISTANCE, MIN_POWER, MAX_POWER);
            shootPower = Range.clip(power, MIN_POWER, MAX_POWER);
            return shootPower;
        }

        return shootPower;

    }

    public double scaleServoPos(double distance){

        if(distance != 0){
            double power = Range.scale(distance, MIN_DISTANCE, MAX_DISTANCE, MIN_SERVO_POS, MAX_SERVO_POS);
            servoPos = Range.clip(power, MIN_SERVO_POS, MAX_SERVO_POS);
            return servoPos;
        }

        return servoPos;
    }

    public double shoot(double distance){

        basePower = scalePower(distance);
        servoPos = scaleServoPos(distance);

        shooter1.setPower(basePower);
        shooter2.setPower(basePower);

        shooterServo.setPosition(servoPos);

        shootPower = basePower;

        return basePower;
    }

    public void stopShooter(){
        shooter1.setPower(0);
        shooter2.setPower(0);
    }

    public void handleShoot(boolean shooterActive, double distance, TelemetryLogger telemetryLogger){
        if(shooterActive) shoot(distance);
        else stopShooter();
        telemetryLogger.logShootPower(shootPower);
    }

    public boolean isShooterReady(){

        double expectedVelocity = BASE_VELOCITY * basePower;
        //avg velocity
        double velocity = (shooter1.getVelocity() +  shooter2.getVelocity()) / 2;

        return velocity > expectedVelocity - DEADZONE;


    }

}
