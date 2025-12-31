package org.firstinspires.ftc.teamcode.teleops;

import org.firstinspires.ftc.teamcode.subsystems.drivetrain.vroomvroom;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "testDrive")
public class testDrive extends LinearOpMode{
    vroomvroom drive = new vroomvroom();

    double forward, strafe, rotate;
    @Override
    public void runOpMode() throws InterruptedException {
        drive.init(hardwareMap);




        waitForStart();

        if (isStopRequested()) return;
        while (opModeIsActive()){

            forward = gamepad1.left_stick_y;
            strafe = -gamepad1.left_stick_x;
            rotate = -gamepad1.right_stick_x;

            drive.driveFieldRelative(forward, strafe, rotate);
            telemetry.addData("forward", forward);
            telemetry.addData("strafe", strafe);
            telemetry.addData("rotate", rotate);
            telemetry.update();
        }
    }
}
