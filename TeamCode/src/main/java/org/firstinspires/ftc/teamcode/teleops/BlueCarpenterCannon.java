package org.firstinspires.ftc.teamcode.teleops;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.subsystems.Limelight.Slimelight;
import org.firstinspires.ftc.teamcode.subsystems.other.TelemetryLogger;
import org.firstinspires.ftc.teamcode.subsystems.turret.Cannon;
import org.firstinspires.ftc.teamcode.subsystems.indexer.indexing;

import java.util.List;


@TeleOp(name = "Blue Tele Op")
public class BlueCarpenterCannon extends LinearOpMode {

    // test if works
    // fix distance power scaling method
    // separate classes
    // start drivetrain

    private TelemetryLogger telemetryLogger;
    private DcMotorEx turn1;
 //   private Slimelight slimelight;
    private Limelight3A slimelight
    private Cannon cannon;
    double turningPower = 0.0;
    double lastOffset = 0.0;
    int targetedFiducialId;
    private indexing indexer = new indexing();
    final int blueFiducialId = 20;

    @Override
    public void runOpMode() throws InterruptedException{
        // Define motors, make sure Id's match

        slimelight = hardwareMap.get(Limelight3A.class, "slimelight");
        cannon = new Cannon(hardwareMap);
        telemetryLogger = new TelemetryLogger(telemetry);

        turn1 = hardwareMap.get(DcMotorEx.class, "turn1");
        turn1.setDirection(DcMotorSimple.Direction.REVERSE);


        double distance = 0.0;
        double xOffset = 0.0;
        double yOffset = 0.0;
        double shootPower = 0.0;
        double servoPos = 0.0;

        Pose botpose;
        double ty = 0;
        double tx = 0;



        waitForStart();
     //   slimelight.initializeLimelight();


    if (isStopRequested()) return;

    boolean canSee = false;
    boolean shooterActive = false;
    double TICKS_PER_DEG = 1;
    double MAX_ANGLE = 270;
    double MIN_ANGLE = -270;
    double turretTarget = 0.0;
    double kP = 4;

    while (opModeIsActive()){
        indexer.update();

        LLResult result = slimelight.getLatestResult();
        LLStatus status = slimelight.getStatus();

        telemetry.addData("Name", "%s",
                status.getName());
        telemetry.addData("LL", "Temp: %.1fC, CPU: %.1f%%, FPS: %d",
                status.getTemp(), status.getCpu(),(int)status.getFps());

        if (result.isValid()) {
            // Access general information
            Pose3D pose = result.getBotpose();
            Position position = pose.getPosition().toUnit(DistanceUnit.INCH);
            botpose = new Pose(position.x, position.y, pose.getOrientation().getYaw());
            telemetry.addData("tx", result.getTx());
            telemetry.addData("txnc", result.getTxNC());
            telemetry.addData("ty", result.getTy());
            telemetry.addData("tync", result.getTyNC());
            telemetry.addData("Botpose", botpose.toString());

            // Access fiducial results
            List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
            for (LLResultTypes.FiducialResult fr : fiducialResults) {
                telemetry.addData("Fiducial", "ID: %d, Family: %s, X: %.2f, Y: %.2f",
                        fr.getFiducialId(), fr.getFamily(), fr.getTargetXDegrees(), fr.getTargetYDegrees());
            }
        }
        else {
            telemetry.addData("Limelight", "No data available");
        }

        List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
        for (LLResultTypes.FiducialResult fr : fiducialResults) {
            if (fr.getFiducialId() == blueFiducialId) {
                ty = result.getTy();
                tx = result.getTx();
                canSee = true;
                distance = tyToDistanceTree.get(ty);
            }
        }

        turret.setTurretAngle(tx, canSee, gamepad1.right_stick_x, getTurretTargetFromPose(follower.getPose()));










//        slimelight.update(telemetryLogger, blueFiducialId);
//        if (gamepad1.rightBumperWasPressed()) {
//            indexer.startShooting();
//        }
//        LLResult result = slimelight.getResult();
//        distance = slimelight.getDistance(result);
//
//
//        turningPower = slimelight.trackAprilTag(result, blueFiducialId);
//        if (result.getFiducialResults().get(0).getFiducialId() == blueFiducialId) {
//            turretTarget = turn1.getCurrentPosition() + result.getTxNC() * TICKS_PER_DEG;
//            if (turretTarget > MAX_ANGLE) {
//                turretTarget -= 360 * TICKS_PER_DEG;
//            } else if (turretTarget < MIN_ANGLE) {
//                turretTarget += 360 * TICKS_PER_DEG;
//            }
//        }
//
//        turningPower = kP * (turretTarget - turn1.getCurrentPosition());
//
//        xOffset = slimelight.getXOffset(result, blueFiducialId);
//        yOffset = slimelight.getYOffset(result, blueFiducialId);

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
