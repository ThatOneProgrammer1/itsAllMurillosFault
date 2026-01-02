package org.firstinspires.ftc.teamcode.tests;

import org.firstinspires.ftc.teamcode.subsystems.drivetrain.vroomvroom;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "testDrive")
public class testDrive extends LinearOpMode{
    vroomvroom drive = new vroomvroom();

    double forward, strafe, rotate;
    public DcMotorEx intake;
    public double intakePower;

    @Override
    public void runOpMode() throws InterruptedException {
        drive.init(hardwareMap);

        intake = hardwareMap.get(DcMotorEx.class, "intake");
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);



        waitForStart();

        if (isStopRequested()) return;
        while (opModeIsActive()){

            forward = -gamepad1.left_stick_y;
            strafe = gamepad1.left_stick_x;
            rotate = gamepad1.right_stick_x;

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

            drive.driveFieldRelative(forward, strafe, rotate);
            telemetry.addData("forward", forward);
            telemetry.addData("strafe", strafe);
            telemetry.addData("rotate", rotate);
            telemetry.update();
        }
    }
}
