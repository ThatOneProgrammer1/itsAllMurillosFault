package org.firstinspires.ftc.teamcode.susbystems.teleops;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.susbystems.color.ColorDet;
import org.firstinspires.ftc.teamcode.susbystems.misc.Slimelight;
import org.firstinspires.ftc.teamcode.susbystems.shooter.Cannon;
import org.firstinspires.ftc.teamcode.susbystems.misc.TelemetryLogger;


@TeleOp(name = "Blue Tele Op")
public class BlueCarpenterCannon extends LinearOpMode {


    // to do
    // central telemetry class
    // test red shooter
    // shooter ready function prepare for intake

    private Slimelight slimelight;
    private Cannon cannon;
    private TelemetryLogger telemetryLogger;
    private ColorDet color;
    final int blueFiducialId = 20;


    @Override
    public void runOpMode() throws InterruptedException{

        slimelight = new Slimelight(hardwareMap);
        cannon = new Cannon(hardwareMap);
        color = new ColorDet(hardwareMap);
        telemetryLogger = new TelemetryLogger(telemetry);

        waitForStart();
        slimelight.initializeLimelight();

        if (isStopRequested()) return;
        boolean shooterActive = false;

        while (opModeIsActive()){

            slimelight.update(telemetryLogger, blueFiducialId);
            double distance = slimelight.getDistance();


            if(gamepad1.a){
                shooterActive = true;
            }
            if(gamepad1.x){
                shooterActive = false;
            }

            color.detectColor(telemetryLogger);
            cannon.handleShoot(shooterActive, distance, telemetryLogger);

        }
    }



}
