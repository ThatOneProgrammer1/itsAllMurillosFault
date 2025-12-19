package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.helper.Slimelight;
import org.firstinspires.ftc.teamcode.helper.TelemetryLogger;
import org.firstinspires.ftc.teamcode.subsystems.Cannon;
import org.firstinspires.ftc.teamcode.subsystems.indexing;


@TeleOp(name = "Blue Tele Op")
public class BlueCarpenterCannon extends LinearOpMode {

    // test if works
    // fix distance power scaling method
    // separate classes
    // start drivetrain

    private TelemetryLogger telemetryLogger;
    private DcMotorEx turn1;
    private Slimelight slimelight;
    private Cannon cannon;
    double turningPower = 0.0;
    double lastOffset = 0.0;
    int targetedFiducialId;
    private indexing indexer = new indexing();
    final int blueFiducialId = 20;


    @Override
    public void runOpMode() throws InterruptedException{
        // Define motors, make sure Id's match

        slimelight = new Slimelight(hardwareMap);
        cannon = new Cannon(hardwareMap);
        telemetryLogger = new TelemetryLogger(telemetry);

        turn1 = hardwareMap.get(DcMotorEx.class, "turn1");
        turn1.setDirection(DcMotorSimple.Direction.REVERSE);


        double distance = 0.0;
        double xOffset = 0.0;
        double yOffset = 0.0;
        double shootPower = 0.0;
        double servoPos = 0.0;


        waitForStart();
        slimelight.initializeLimelight();


    if (isStopRequested()) return;

    boolean shooterActive = false;
    double TICKS_PER_DEG = 1;
    double MAX_ANGLE = 270;
    double MIN_ANGLE = -270;
    double turretTarget = 0.0;
    double kP = 4;

    while (opModeIsActive()){
        indexer.update();

        if (gamepad1.rightBumperWasPressed()) {
            indexer.startShooting();
        }
        LLResult result = slimelight.getResult();
        distance = slimelight.getDistance(result);
        turningPower = slimelight.trackAprilTag(result, blueFiducialId);
        if (result.getFiducialResults().get(0).getFiducialId() == blueFiducialId) {
            turretTarget = turn1.getCurrentPosition() + result.getTxNC() * TICKS_PER_DEG;
            if (turretTarget > MAX_ANGLE) {
                turretTarget -= 360 * TICKS_PER_DEG;
            } else if (turretTarget < MIN_ANGLE) {
                turretTarget += 360 * TICKS_PER_DEG;
            }
        }

        turningPower = kP * (turretTarget - turn1.getCurrentPosition());

        xOffset = slimelight.getXOffset(result, blueFiducialId);
        yOffset = slimelight.getYOffset(result, blueFiducialId);

        if(gamepad1.a){
            shooterActive = true;
        }
        if(gamepad1.x){
            shooterActive = false;
        }


        if(shooterActive){
            cannon.shoot(distance);
        }
        else if (!shooterActive){
            cannon.stopShooter();
        }

        cannon.handleShoot(shooterActive, distance, telemetryLogger);

        turn1.setPower(turningPower);

        }
    }
}
