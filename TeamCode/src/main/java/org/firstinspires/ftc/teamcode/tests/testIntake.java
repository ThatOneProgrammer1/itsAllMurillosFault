package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "testIntake")
public class testIntake extends LinearOpMode {

    public DcMotorEx intake;
    public double intakePower;

    @Override
    public void runOpMode(){

        intake = hardwareMap.get(DcMotorEx.class, "intake");
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);



        waitForStart();

        if(isStopRequested()) return;
        while (opModeIsActive()) {

            if(gamepad1.aWasPressed()){
                intakePower = 0;
            }
            if(gamepad1.bWasPressed()){
                intakePower = 0.4;
            }
            if(gamepad1.xWasPressed()){
                intakePower = 1;
            }
            if(gamepad1.yWasPressed()){
                intakePower = 0.8;
            }
            intake.setPower(intakePower);
            telemetry.addData("intake power", intakePower);
        }
    }
}
