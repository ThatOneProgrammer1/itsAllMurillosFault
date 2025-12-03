package org.firstinspires.ftc.teamcode.helper;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

public class Slimelight {

    private Limelight3A slimelight;
    private Tracking tracking;

    //consts
    private final double OFFSET_ZONE = 4.0;
    private final double MAX_LEFT_TURN = -0.25;
    private final double MAX_RIGHT_TURN = 0.25;

    private final double MIN_OFFSET = -30;
    private final double MAX_OFFSET = 30;
    private final double MIN_TURN_THRESHOLD = 0.075;

    private double distance = 0;

    public Slimelight(HardwareMap hardwareMap){
        slimelight = hardwareMap.get(Limelight3A.class, "slimelight");
        tracking = new Tracking(hardwareMap);
    }


    public double getDistance(){
        return distance;
    }


    public double getXOffset(LLResult result, int targetedFiducialId){

        LLResultTypes.FiducialResult tag = getLatestResult(result);

        if(tag != null && tag.getFiducialId() == targetedFiducialId){
            return tag.getTargetXDegrees();
        }

        return 0;
    }


    public double trackAprilTag(LLResult result, int targetedFiducialId){

        double offset = getXOffset(result, targetedFiducialId);

        if(Math.abs(offset) < OFFSET_ZONE){
            return 0;
        }
        else{
            double scaledOffset = Range.scale(offset, MIN_OFFSET, MAX_OFFSET, MAX_LEFT_TURN , MAX_RIGHT_TURN);
            double clippedOffset = Range.clip(scaledOffset, MAX_LEFT_TURN, MAX_RIGHT_TURN); // use for power of turn motor

            if(Math.abs(clippedOffset) < MIN_TURN_THRESHOLD){
                return (clippedOffset * 3);
            }
            else{
                return clippedOffset;
            }
        }

    }


    public double getYOffset(LLResult result, int targetedFiducial){

        LLResultTypes.FiducialResult tag = getLatestResult(result);

        if(tag != null && tag.getFiducialId() == targetedFiducial){
            return tag.getTargetYDegrees();
        }

        return 0;
    }


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


    public LLResultTypes.FiducialResult getLatestResult(LLResult result){
        if(result.isValid() && !result.getFiducialResults().isEmpty()){
            return result.getFiducialResults().get(0);
        }
        return null;
    }


    public void initializeLimelight(){
        slimelight.setPollRateHz(100);
        slimelight.start();
    }


    public LLResult getResult(){
        return slimelight.getLatestResult();
    }


    public void update(Telemetry telemetry, int fiducialId){
        LLResult result = getResult();

        distance = getDistance(result);
        double xOffset = getXOffset(result, fiducialId);
        double yOffset = getYOffset(result, fiducialId);
        double turningPower = trackAprilTag(result, fiducialId);

        tracking.turnMotor(turningPower);

        telemetry.addData("Distance", distance);
        telemetry.addData("X offset", xOffset);
        telemetry.addData("Fiducial ID", fiducialId);
        telemetry.addData("Y offset", yOffset);
        telemetry.update();
    }

}
