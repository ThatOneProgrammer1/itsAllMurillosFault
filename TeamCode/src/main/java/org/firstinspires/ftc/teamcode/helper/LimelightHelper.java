package org.firstinspires.ftc.teamcode.helper;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

public class LimelightHelper {

    private Limelight3A slimelight;
    private final double offset_zone = 4.0;

    public LimelightHelper(HardwareMap hardwareMap){
        slimelight = hardwareMap.get(Limelight3A.class, "slimelight");
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

    public double getYOffset(LLResult result){

        LLResultTypes.FiducialResult tag = getLatestResult(result);

        if(tag != null){
            return tag.getTargetYDegrees();
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

}
