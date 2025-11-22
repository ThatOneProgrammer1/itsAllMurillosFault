package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;



@TeleOp
public class carpenterCannon extends LinearOpMode {

    private DcMotorEx shooter1;
    private DcMotorEx shooter2;
    private DcMotorEx turn1;
    private Limelight3A limelight;
    private final int offset_zone = 8;
    double turningPower = 0.0;

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

        return -1;
    }

    // method to find the angle offset of limelight for tracking
    public double getXOffset(LLResult result){

        LLResultTypes.FiducialResult tag = getLatestResult(result);

        if(tag != null){
            return tag.getTargetXDegrees();
        }

        return -1;
    }

    public double trackAprilTag(LLResult result){

        double offset = getXOffset(result);

        if(Math.abs(offset) < offset_zone){
            turningPower = 0;
        }
        else if(offset > offset_zone){
            turningPower = 0.35;
        }
        else if(offset < -offset_zone){
            turningPower = -0.35;
        }

        return offset;

    }

    // same as above
    public double getYOffset(LLResult result){

        LLResultTypes.FiducialResult tag = getLatestResult(result);

        if(tag != null){
            return tag.getTargetYDegrees();
        }

        return -1;
    }

    public LLResultTypes.FiducialResult getLatestResult(LLResult result){
        if(result.isValid() && !result.getFiducialResults().isEmpty()){
            return result.getFiducialResults().get(0);
        }
        return null;
    }


    @Override
    public void runOpMode() throws InterruptedException{
        // Define motors, make sure Id's match
        shooter1 = hardwareMap.get(DcMotorEx.class, "shooter1");
        shooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter1.setDirection(DcMotorSimple.Direction.REVERSE);

        shooter2 = hardwareMap.get(DcMotorEx.class, "shooter2");
        shooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter2.setDirection(DcMotorSimple.Direction.REVERSE);

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        turn1 = hardwareMap.get(DcMotorEx.class, "turn1");


        double shootPower = 0.0;
        double distance = 0.0;
        double xOffset = 0.0;
        // moved turning power to be class variable btw

        limelight.setPollRateHz(100);
        waitForStart();
        limelight.start();

    if (isStopRequested()) return;

    while (opModeIsActive()){


        LLResult result = limelight.getLatestResult();
        distance = getDistance(result);
        xOffset = trackAprilTag(result);



        if (gamepad1.xWasPressed()){
            turningPower = 0;
            shootPower = 0;
        } else if (gamepad1.aWasPressed()){
            turningPower = 0.1;
            shootPower = 0.5;
        } else if (gamepad1.bWasPressed()){
            turningPower = -0.1;
            shootPower = 0.35;
        } else if (gamepad1.yWasPressed()){
            shootPower = 0.65;
        }

        turn1.setPower(turningPower);

        shooter1.setPower(shootPower);
        shooter2.setPower(shootPower);
        telemetry.addData("Shooter power", shootPower);
        telemetry.addData("Turning Power", turningPower);
        telemetry.addData("Distance", distance);
        telemetry.addData("Target angle", xOffset);
        telemetry.update();
        }
    }



}
