package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class carpenterCannon extends LinearOpMode {
    private DcMotorEx shooter1;
    private DcMotorEx shooter2;
    private DcMotorEx turn1;

    @Override
    public void runOpMode() throws InterruptedException{
        // Define motors, make sure Id's match
        shooter1 = hardwareMap.get(DcMotorEx.class, "shooter1");
        shooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter1.setDirection(DcMotorSimple.Direction.REVERSE);

        shooter2 = hardwareMap.get(DcMotorEx.class, "shooter2");
        shooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter2.setDirection(DcMotorSimple.Direction.REVERSE);

      //  turn1 = hardwareMap.get(DcMotorEx.class, "turn1");

        double turningPower = 0;
        double shootPower = 0;

        waitForStart();

    if (isStopRequested()) return;

    while (opModeIsActive()){
        if (gamepad1.xWasPressed()){
            turningPower = 0;
            shootPower = 0;
        } else if (gamepad1.aWasPressed()){
            turningPower = 0.1;
            shootPower = 0.5;
        } else if (gamepad1.bWasPressed()){
            turningPower = -0.1;
            shootPower = 0.35;
        } else if (gamepad1.yWasPressed()){
            shootPower = 0.65;
        }

       // turn1.setPower(turningPower);
        shooter1.setPower(shootPower);
        shooter2.setPower(shootPower);
        telemetry.addData("Shooter power", shootPower);
        telemetry.addData("Turning Power", turningPower);
        telemetry.update();
        }
    }



}
