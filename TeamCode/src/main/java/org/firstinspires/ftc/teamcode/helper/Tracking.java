package org.firstinspires.ftc.teamcode.helper;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Tracking {

    private DcMotorEx turn1;
    public Tracking(HardwareMap hw){
        turn1 = hw.get(DcMotorEx.class, "turn1");
        turn1.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void turnMotor(double power){
        turn1.setPower(power);
    }

}
