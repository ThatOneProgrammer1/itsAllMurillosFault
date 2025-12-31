package org.firstinspires.ftc.teamcode.subsystems.turret;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class turret {

    private DcMotorEx turret;

    public turret(HardwareMap hardwareMap){

        turret = hardwareMap.get(DcMotorEx.class, "turner");
        turret.setDirection(DcMotorSimple.Direction.REVERSE);
        turret.setTargetPosition(0);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);


    }

}
