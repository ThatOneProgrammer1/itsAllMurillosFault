package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.helper.LimelightHelper;

import java.util.HashSet;


@TeleOp(name = "Red Tele Op")
public class RedCarpenterCannon extends LinearOpMode {


    // fix limelight tracking onto both april tags
    // fix distance power scaling method
    // separate classes
    // start drivetrain

    private DcMotorEx shooter1;
    private DcMotorEx shooter2;
    private DcMotorEx turn1;
    private LimelightHelper slimelight;
    double turningPower = 0.0;
    double lastOffset = 0.0;
    int targetedFiducialId;
    final int redFiducialId = 20;


    // delete these two later
    public double testShootDistanceScale(double distance){
        double power = Range.scale(distance, 30, 115, 0.5, 0.65);
        return Range.clip(power, 0.5, 0.65);
    }

    // method to find our distance when we wanna scale power
    public double getDistance(LLResult result){

        if(result.isValid() && !result.getFiducialResults().isEmpty()){

            LLResultTypes.FiducialResult tag = result.getFiducialResults().get(0);

            Pose3D tagPose = tag.getTargetPoseCameraSpace();

            Position pos = tagPose.getPosition();
            Position inches = pos.toUnit(DistanceUnit.INCH);

            double x = inches.x;
            double y = inches.y;
            double z = inches.z;

            return Math.sqrt(x*x + y*y + z*z);

        }

        return 0;
    }

    // method to find the angle offset of limelight for tracking


    @Override
    public void runOpMode() throws InterruptedException{
        // Define motors, make sure Id's match

//        shooter1 = hardwareMap.get(DcMotorEx.class, "shooter1");
//        shooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        shooter1.setDirection(DcMotorSimple.Direction.REVERSE);
//
//        shooter2 = hardwareMap.get(DcMotorEx.class, "shooter2");
//        shooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        shooter2.setDirection(DcMotorSimple.Direction.REVERSE);

        slimelight = new LimelightHelper(hardwareMap);

        turn1 = hardwareMap.get(DcMotorEx.class, "turn1");
        turn1.setDirection(DcMotorSimple.Direction.REVERSE);


        double shootPower = 0.0;
        double distance = 0.0;
        double xOffset = 0.0;


        waitForStart();
        slimelight.initializeLimelight();


        if (isStopRequested()) return;

        boolean shooterActive = false;

        while (opModeIsActive()){

            LLResult result = slimelight.getResult();

            distance = getDistance(result);
            turningPower = slimelight.trackAprilTag(result, redFiducialId);
            xOffset = slimelight.getXOffset(result, redFiducialId);
            shootPower = testShootDistanceScale(distance);


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


            //dont use rn
            if(gamepad1.a){
                shooterActive = true;
            }
            if(gamepad1.x){
                shooterActive = false;
            }

            turn1.setPower(turningPower);

            telemetry.addData("Shooter power", shootPower);
            telemetry.addData("Turning Power", turningPower);
            telemetry.addData("Distance", distance);
            telemetry.addData("Target angle", xOffset);
            telemetry.addData("Fiducial ID", targetedFiducialId);
            telemetry.update();
        }
    }



}
