package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "testing")
public class testingServo extends LinearOpMode {
    // testing servos

    private Servo sorter1;
    private Servo sorter2;
    private Servo sorter3;

    private DcMotorEx shooter1;
    private DcMotorEx shooter2;

    @Override
    public void runOpMode() throws InterruptedException{
        shooter1 = hardwareMap.get(DcMotorEx.class, "shooter1");
        shooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter1.setDirection(DcMotorSimple.Direction.REVERSE);

        shooter2 = hardwareMap.get(DcMotorEx.class, "shooter2");
        shooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter2.setDirection(DcMotorSimple.Direction.REVERSE);

        sorter1 = hardwareMap.get(Servo.class, "sorter1");
        sorter2 = hardwareMap.get(Servo.class, "sorter2");
        sorter3 = hardwareMap.get(Servo.class, "sorter3");

        double shooterPower = 0;
        String servoPlace = "Idle";
        String servoPlace2 = "Idle";
        String shooterStatus = "Idle";
        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()){
            if (gamepad1.aWasPressed()){
                shooterPower = 0.5;
                shooterStatus = "Powering";
            }
            if (gamepad1.xWasPressed()){
                shooterPower = 0;
                shooterStatus = "Stopped";
            }

            if (gamepad1.yWasPressed()){
                sorter1.setPosition(0.667);
            }

            if (gamepad1.bWasPressed()){
                sorter1.setPosition(0.225);
            }

            if (gamepad1.dpad_up){
                sorter2.setPosition(0.85);
                servoPlace = "Up";
            }
            if (gamepad1.dpad_down){
                sorter2.setPosition(0.125);
                servoPlace = "Down";
            }

            if (gamepad1.dpad_left){
                sorter3.setPosition(0.05);
                servoPlace2 = "Up";
            }

            if (gamepad1.dpad_right){
                sorter3.setPosition(0.52);
                servoPlace2 = "Down";
            }

         shooter1.setPower(shooterPower);
         shooter2.setPower(shooterPower);
         telemetry.addData("Shooter set power", shooterPower);
         telemetry.addData("Servo pose", servoPlace);
         telemetry.addData("Servo pose 2", servoPlace2);
         telemetry.addData("Shooter Status", shooterStatus);
         telemetry.update();
        }

    }

}
