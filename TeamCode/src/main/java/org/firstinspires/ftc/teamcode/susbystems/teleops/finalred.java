package org.firstinspires.ftc.teamcode.susbystems.teleops;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.susbystems.color.ColorDet;
import org.firstinspires.ftc.teamcode.susbystems.intake.Intake;
import org.firstinspires.ftc.teamcode.susbystems.limelight.Slimelight;
import org.firstinspires.ftc.teamcode.susbystems.shooter.Cannon;
import org.firstinspires.ftc.teamcode.susbystems.misc.TelemetryLogger;


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
    private Intake intake;
    final int redFiducialId = 24;
    private ColorDet colorDet;

    @Override
    public void runOpMode() throws InterruptedException {

        slimelight = new Slimelight(hardwareMap);
        cannon = new Cannon(hardwareMap);
        telemetryLogger = new TelemetryLogger(telemetry);
        intake = new Intake(hardwareMap);
        colorDet = new ColorDet(hardwareMap);

        runtime.reset();
        slimelight.initializeLimelight();
        waitForStart();

        if (isStopRequested()) return;
        boolean shooterActive = false;
        Mode mode = Mode.RANDOM;

        while (opModeIsActive()){

            if(runtime.seconds() > 120){
                mode = Mode.SORTING;
            }

            double distance = slimelight.update(telemetryLogger, redFiducialId);

            LLResult res = slimelight.getResult();

            if(gamepad1.a){
                shooterActive = true;
            }
            if(gamepad1.x){
                shooterActive = false;
            }

            if(gamepad1.b){
                cannon.testMaxVelocity();
            }


            if(shooterActive && cannon.isShooterReady()){

                if(mode == Mode.RANDOM){
                    intake.liftRandomBalls();
                }

            }

            if(res.isValid() && !res.getFiducialResults().isEmpty()){
                if (res.getFiducialResults().get(0) != null) {
                    telemetryLogger.log("Detected ID", String.valueOf(res.getFiducialResults().get(0).getFiducialId()));
                }
            }



            cannon.handleShoot(shooterActive, distance, telemetryLogger);
            telemetryLogger.log("Motor Velocity", String.valueOf(cannon.getAvgVelocity()));
            telemetryLogger.log("Runtime", String.valueOf(runtime.seconds()));
//            telemetryLogger.log("Motif", (seenMotif ? "Seen" : "Not Seen"));
            telemetryLogger.log("Result", (res.isValid() ? "Valid" : "Invalid"));
            telemetry.update();
        }


    }
}



