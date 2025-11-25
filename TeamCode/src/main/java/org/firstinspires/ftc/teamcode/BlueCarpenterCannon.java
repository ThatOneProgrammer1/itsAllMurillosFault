package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.helper.LimelightHelper;
import org.firstinspires.ftc.teamcode.helper.Shooter;

import java.util.HashSet;


@TeleOp(name = "Blue Tele Op")
public class BlueCarpenterCannon extends LinearOpMode {

    // test if works
    // fix distance power scaling method
    // separate classes
    // start drivetrain

    private DcMotorEx shooter1;
    private DcMotorEx shooter2;
    private DcMotorEx turn1;
    private LimelightHelper slimelight;

    private Shooter cannon;
    double turningPower = 0.0;
    double lastOffset = 0.0;
    int targetedFiducialId;
    final int blueFiducialId = 20;


    @Override
    public void runOpMode() throws InterruptedException{
        // Define motors, make sure Id's match

        slimelight = new LimelightHelper(hardwareMap);
        cannon = new Shooter(hardwareMap);

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

    while (opModeIsActive()){

        LLResult result = slimelight.getResult();
        distance = slimelight.getDistance(result);
        turningPower = slimelight.trackAprilTag(result, blueFiducialId);

        xOffset = slimelight.getXOffset(result, blueFiducialId);
        yOffset = slimelight.getYOffset(result, blueFiducialId);




//        if (gamepad1.xWasPressed()){
//            turningPower = 0;
//            shootPower = 0;
//        } else if (gamepad1.aWasPressed()){
//            turningPower = 0.1;
//            shootPower = 0.5;
//        } else if (gamepad1.bWasPressed()){
//            turningPower = -0.1;
//            shootPower = 0.35;
//        } else if (gamepad1.y){
//            shootPower = 0.65;
//        }

        if(gamepad1.a){
            shooterActive = true;
        }
        if(gamepad1.x){
            shooterActive = false;
        }

        if(shooterActive){
            shootPower = cannon.shoot(distance);
        }
        else if (!shooterActive){
            cannon.stopShooter();
        }



        turn1.setPower(turningPower);

        telemetry.addData("Shooter power", shootPower);
        telemetry.addData("Turning Power", turningPower);
        telemetry.addData("Distance", distance);
        telemetry.addData("X offset", xOffset);
        telemetry.addData("Fiducial ID", targetedFiducialId);
        telemetry.addData("Y offset", yOffset);
        telemetry.update();
        }
    }



}
