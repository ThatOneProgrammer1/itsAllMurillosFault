package org.firstinspires.ftc.teamcode.susbystems.intake;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {

    private DcMotor intakeMotor;

    public Intake(HardwareMap hw){
        intakeMotor = hw.get(DcMotor.class, "intake");
    }

    public void startIntake(){
        intakeMotor.setPower(1);
    }

    public void stopIntake(){
        intakeMotor.setPower(0);
    }

    public void intakeTest(boolean intakeActive){
        if(intakeActive) startIntake();
        else stopIntake();
    }

}
