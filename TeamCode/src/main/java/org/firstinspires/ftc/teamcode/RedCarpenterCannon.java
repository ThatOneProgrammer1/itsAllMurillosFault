package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.helper.Slimelight;
import org.firstinspires.ftc.teamcode.helper.Cannon;
import org.firstinspires.ftc.teamcode.helper.TelemetryLogger;


@TeleOp(name = "Red Tele Op")
public class RedCarpenterCannon extends LinearOpMode {


    // to do
    // central telemetry class
    // test red shooter
    // shooter ready function prepare for intake

    private Slimelight slimelight;
    private Cannon cannon;
    private TelemetryLogger telemetryLogger;
    final int redFiducialId = 24;


    @Override
    public void runOpMode() throws InterruptedException{

        slimelight = new Slimelight(hardwareMap);
        cannon = new Cannon(hardwareMap);
        telemetryLogger = new TelemetryLogger(telemetry);

        waitForStart();
        slimelight.initializeLimelight();

        if (isStopRequested()) return;
        boolean shooterActive = false;

        while (opModeIsActive()){

            slimelight.update(telemetryLogger, redFiducialId);
            double distance = slimelight.getDistance();


            if(gamepad1.a){
                shooterActive = true;
            }
            if(gamepad1.x){
                shooterActive = false;
            }

            cannon.handleShoot(shooterActive, distance, telemetryLogger);

        }
    }



}
