package org.firstinspires.ftc.teamcode.susbystems.indexing;

import com.qualcomm.robotcore.hardware.Servo;

public class Sorter {

    private Servo servo;
    private double restPos;
    private double shootPos;

    public Sorter(Servo servo, double restPosition, double shootPosition){
        this.servo = servo;
        this.restPos = restPosition;
        this.shootPos = shootPosition;
    }

    public double getRestPosition(){
        return restPos;
    }

    public double getShootPosition(){
        return shootPos;
    }

    public void setPosition(double pos){
        servo.setPosition(pos);
    }

}
