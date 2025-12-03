package org.firstinspires.ftc.teamcode.helper;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.helper.intefaces.Shooter;

public class Cannon implements Shooter {

    private DcMotorEx shooter1;
    private DcMotorEx shooter2;
    private Servo shooterServo;

    private double basePower = 0.0;
    private double servoPos = 0.0;
    private double shootPower = 0.0;

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


    @Override
    public double shoot(double distance){

        basePower = scalePower(distance);
        servoPos = scaleServoPos(distance);

        shooter1.setPower(basePower);
        shooter2.setPower(basePower);

        shooterServo.setPosition(servoPos);

        shootPower = basePower;

        return basePower;
    }


    @Override
    public void stopShooter(){
        shooter1.setPower(0);
        shooter2.setPower(0);
    }

    @Override
    public void handleShoot(boolean shooterActive, double distance, Telemetry telemetry){
        if(shooterActive) shoot(distance);
        else stopShooter();
        telemetry.addData("Shoot Power", shootPower);
    }

    @Override
    public boolean isShooterReady(){

        double expectedVelocity = BASE_VELOCITY * basePower;
        //avg velocity
        double velocity = (shooter1.getVelocity() +  shooter2.getVelocity()) / 2;

        return velocity > expectedVelocity - DEADZONE;


    }

}
