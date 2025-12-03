package org.firstinspires.ftc.teamcode.helper;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Const;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

public class LimelightHelper {

    private Limelight3A slimelight;
    private Tracking tracking;
    private final double offset_zone = 4.0;
    double distance = 0;
    double xOffset = 0;
    double yOffset = 0;
    double turningPower = 0;


    public LimelightHelper(HardwareMap hardwareMap){
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

        if(Math.abs(offset) < offset_zone){
            return 0;
        }
        else{
            double scaledOffset = Range.scale(offset, -30, 30, -0.25, 0.25);
            double clippedOffset = Range.clip(scaledOffset, -0.25, 0.25); // use for power of turn motor

            if(Math.abs(clippedOffset) < 0.075){
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

    public void Run(Telemetry telemetry, int fiducialId){
        LLResult result = getResult();

        distance = getDistance(result);
        xOffset = getXOffset(result, fiducialId);
        yOffset = getYOffset(result, fiducialId);
        turningPower = trackAprilTag(result, fiducialId);

        tracking.turnMotor(turningPower);

        telemetry.addData("Distance", distance);
        telemetry.addData("X offset", xOffset);
        telemetry.addData("Fiducial ID", fiducialId);
        telemetry.addData("Y offset", yOffset);
        telemetry.update();
    }

}
