package org.firstinspires.ftc.teamcode.susbystems.teleops;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.susbystems.color.ColorDet;
import org.firstinspires.ftc.teamcode.susbystems.intake.Intake;
import org.firstinspires.ftc.teamcode.susbystems.limelight.Slimelight;
import org.firstinspires.ftc.teamcode.susbystems.shooter.Cannon;
import org.firstinspires.ftc.teamcode.susbystems.misc.TelemetryLogger;


@TeleOp(name = "Blue Tele Op")
public class BlueCarpenterCannon extends LinearOpMode {

    private Slimelight slimelight;
    private Cannon cannon;
    private TelemetryLogger telemetryLogger;
    private Intake intake;
    private ColorDet colorDet;
    final int blueFiducialId = 20;

    @Override
    public void runOpMode() throws InterruptedException{

        slimelight = new Slimelight(hardwareMap);
        cannon = new Cannon(hardwareMap);
        telemetryLogger = new TelemetryLogger(telemetry);
        intake = new Intake(hardwareMap);
        colorDet = new ColorDet(hardwareMap);


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
            seenMotif = slimelight.fetchMotifId(colorDet);

            if(shooterActive && seenMotif && shooterReady){
                int servoIndex = colorDet.shootNextBall();
                if(servoIndex != -1){
                    intake.liftBall(servoIndex);
                }
            }

            if (!seenMotif){
                telemetryLogger.log("Motif Tracked", "False");
            }else if(!colorDet.ballColorsMatchMotif()){
                telemetryLogger.log("Motif Tracked", "True");
                telemetryLogger.log("Balls in motif order", "False");
            }
            else{
                telemetryLogger.log("Motif Tracked", "True");
                telemetryLogger.log("Balls in motif order", "True");
            }


        }
    }



}
