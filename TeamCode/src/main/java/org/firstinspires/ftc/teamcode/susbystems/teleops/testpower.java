package org.firstinspires.ftc.teamcode.susbystems.teleops;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.susbystems.limelight.Slimelight;
import org.firstinspires.ftc.teamcode.susbystems.misc.TelemetryLogger;
import org.firstinspires.ftc.teamcode.susbystems.shooter.Cannon;

@TeleOp(name = "yes ttest the ppower plzz")
public class testpower extends LinearOpMode {

    private Cannon cannon;
    private Slimelight slimelight;
    private TelemetryLogger telemetryLogger;

    @Override
    public void runOpMode() throws InterruptedException {

        telemetryLogger = new TelemetryLogger(telemetry);
        slimelight =  new Slimelight(hardwareMap);
        cannon = new Cannon(hardwareMap);

        slimelight.initializeLimelight();
        waitForStart();

        if (isStopRequested()) return;

        double power = 0.0;

        boolean up = false;
        boolean down = false;

        while (opModeIsActive()) {

            double distance = slimelight.update(telemetryLogger, 24);

            if (gamepad1.a) power = .25;
            if (gamepad1.b) power = .5;
            if (gamepad1.x) power = .75;
            if (gamepad1.y) power = 1;

            if (gamepad1.dpad_up && !up) {
                power += .01;
                up = true;
            }
            if (gamepad1.dpad_down && !down) {
                power -= .01;
                down = true;
            }

            if (!gamepad1.dpad_up) up = false;
            if (!gamepad1.dpad_down) down = false;

            double temp = cannon.setMotorPowers(power);
            telemetry.addData("Power", String.valueOf(temp));
            telemetry.addData("Servo Pos", String.valueOf(cannon.getServoPos()));
            telemetry.update();
        }
    }
}