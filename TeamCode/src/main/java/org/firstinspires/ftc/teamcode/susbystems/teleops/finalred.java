package org.firstinspires.ftc.teamcode.susbystems.teleops;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.susbystems.color.ColorDet;
import org.firstinspires.ftc.teamcode.susbystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.susbystems.indexing.Indexer;
import org.firstinspires.ftc.teamcode.susbystems.intake.Intake;
import org.firstinspires.ftc.teamcode.susbystems.limelight.Slimelight;
import org.firstinspires.ftc.teamcode.susbystems.shooter.Cannon;
import org.firstinspires.ftc.teamcode.susbystems.misc.TelemetryLogger;
import org.firstinspires.ftc.teamcode.susbystems.shooter.Tracking;


@TeleOp(name = "final red yesss")
public class finalred extends LinearOpMode {

    // not red its blue

    public enum Mode {
        SORTING,
        RANDOM
    }

    private ElapsedTime runtime = new ElapsedTime();
    private Slimelight slimelight;
    private Cannon cannon;
    private TelemetryLogger telemetryLogger;
    private Indexer indexer;
    final int redFiducialId = 24;
    private Drivetrain drivetrain;
    private Intake intake;

    @Override
    public void runOpMode() throws InterruptedException {

        slimelight = new Slimelight(hardwareMap);
        cannon = new Cannon(hardwareMap);
        telemetryLogger = new TelemetryLogger(telemetry);
        indexer = new Indexer(hardwareMap, telemetryLogger);
        intake = new Intake(hardwareMap);
        drivetrain = new Drivetrain();
        drivetrain.init(hardwareMap);

        runtime.reset();
        slimelight.initializeLimelight();
        waitForStart();

        if (isStopRequested()) return;
        boolean shooterActive = false;
        boolean intakeActive = false;
        Mode mode = Mode.RANDOM;

        while (opModeIsActive()){

            LLStatus status = slimelight.getStatus();

            telemetry.addData("Name", "%s", status.getName());

            telemetry.addData("LL", "Temp: %.1fC, CPU: %.1f%%, FPS: %d", status.getTemp(), status.getCpu(),(int)status.getFps());

            if(runtime.seconds() > 120){
                mode = Mode.SORTING;
            }

            double distance = slimelight.update(telemetryLogger, redFiducialId);

            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double rx = gamepad1.right_stick_x;

            drivetrain.driveFieldRelative(y, x, rx);

            LLResult res = slimelight.getResult();

            if(gamepad1.a){
                indexer.startShooting();
                shooterActive = true;
            }
            if(gamepad1.x){
                shooterActive = false;
            }

            if(gamepad1.b){
                cannon.testMaxVelocity();
            }

            if(gamepad1.y){
                intakeActive = !intakeActive;
            }


            if(shooterActive && cannon.isShooterReady()){
                indexer.update();
            }

            if(res.isValid() && !res.getFiducialResults().isEmpty()){
                if (res.getFiducialResults().get(0) != null) {
                    telemetryLogger.log("Detected ID", String.valueOf(res.getFiducialResults().get(0).getFiducialId()));
                }
            }

            intake.intakeTest(intakeActive);

            cannon.handleShoot(shooterActive, distance, telemetryLogger);
            telemetryLogger.log("Motor Velocity", String.valueOf(cannon.getAvgVelocity()));
//            telemetryLogger.log("Runtime", String.valueOf(runtime.seconds()));
//            telemetryLogger.log("Motif", (seenMotif ? "Seen" : "Not Seen"));
            telemetryLogger.log("Result", (res.isValid() ? "Valid" : "Invalid"));
            telemetry.update();
        }


    }
}



