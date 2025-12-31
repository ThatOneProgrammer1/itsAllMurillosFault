package org.firstinspires.ftc.teamcode.susbystems.intake;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Lifter {

    private Servo servo;
    private double openPos;
    private double closePos;
    public boolean lifting = false;
    public ElapsedTime timer = new ElapsedTime();

    public Lifter(Servo servo, double openPos, double closePos){
        this.servo = servo;
        this.openPos = openPos;
        this.closePos = closePos;
    }

    public double getClosePos(){
        return closePos;
    }

    public double getOpenPos(){
        return openPos;
    }

    public void moveServoPos(double pos){
        servo.setPosition(pos);
    }

}
