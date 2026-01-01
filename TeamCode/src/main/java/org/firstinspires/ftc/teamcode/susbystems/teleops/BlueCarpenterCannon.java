package org.firstinspires.ftc.teamcode.susbystems.teleops;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.susbystems.indexing.Indexer;
import org.firstinspires.ftc.teamcode.susbystems.limelight.Slimelight;
import org.firstinspires.ftc.teamcode.susbystems.shooter.Cannon;
import org.firstinspires.ftc.teamcode.susbystems.misc.TelemetryLogger;


@TeleOp(name = "Blue Tele Op")
public class BlueCarpenterCannon extends LinearOpMode {

    private Slimelight slimelight;
    private Cannon cannon;
    private TelemetryLogger telemetryLogger;
    private Indexer indexer;
    final int blueFiducialId = 20;

    @Override
    public void runOpMode() throws InterruptedException{

        slimelight = new Slimelight(hardwareMap);
        cannon = new Cannon(hardwareMap);
        telemetryLogger = new TelemetryLogger(telemetry);
        indexer = new Indexer(hardwareMap, telemetryLogger);


        waitForStart();
        slimelight.initializeLimelight();

        if (isStopRequested()) return;
        boolean shooterActive = false;
        boolean seenMotif = false;

        while (opModeIsActive()){

            slimelight.update(telemetryLogger, blueFiducialId);
            double distance = slimelight.getDistance();

            if(gamepad1.a){
                shooterActive = true;
            }
            if(gamepad1.x){
                shooterActive = false;
            }
            boolean shooterReady = cannon.isShooterReady();

            cannon.handleShoot(shooterActive, distance, telemetryLogger);


        }
    }



}
