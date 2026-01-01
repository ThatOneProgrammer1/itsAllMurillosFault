package org.firstinspires.ftc.teamcode.susbystems.limelight;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.susbystems.misc.TelemetryLogger;
import org.firstinspires.ftc.teamcode.susbystems.shooter.Tracking;

public class Slimelight {

    private Limelight3A slimelight;
    private Tracking tracking;

    private final double OFFSET_ZONE = 3;
    private final double MAX_LEFT_TURN = -0.5;
    private final double MAX_RIGHT_TURN = 0.5;
    private final double MIN_OFFSET = -30;
    private final double MAX_OFFSET = 30;
    private final double MIN_TURN_THRESHOLD = 0.075;

    private double distance = 0;

    public Slimelight(HardwareMap hardwareMap) {
        slimelight = hardwareMap.get(Limelight3A.class, "slimelight");
        tracking = new Tracking(hardwareMap);
    }

    public double getDistance() {
        return distance;
    }

    public LLStatus getStatus(){
        return slimelight.getStatus();
    }

    public double getXOffset(LLResult result, int targetedFiducialId) {
        LLResultTypes.FiducialResult tag = getResultForTracking(result, targetedFiducialId);

        if (tag != null && tag.getFiducialId() == targetedFiducialId) {
            return tag.getTargetXDegrees();
        }

        return 0;
    }

    public double trackAprilTag(LLResult result, int targetedFiducialId) {
        double offset = getXOffset(result, targetedFiducialId);

        if (Math.abs(offset) < OFFSET_ZONE) {
            return 0;
        } else {
            double scaledOffset = Range.scale(offset, MIN_OFFSET, MAX_OFFSET, MAX_LEFT_TURN, MAX_RIGHT_TURN);
            double clippedOffset = Range.clip(scaledOffset, MAX_LEFT_TURN, MAX_RIGHT_TURN);

            double MIN_MOVEMENT_POWER = 0.25;

            if (Math.abs(clippedOffset) < MIN_TURN_THRESHOLD) {
                clippedOffset = clippedOffset * 1.5;
            }

            if (Math.abs(clippedOffset) < MIN_MOVEMENT_POWER) {
                clippedOffset = Math.copySign(MIN_MOVEMENT_POWER, clippedOffset);
            }

            return clippedOffset;
        }
    }

    public double getDistance(LLResult result, int targetId) {
        if (result.isValid() && !result.getFiducialResults().isEmpty()) {
            LLResultTypes.FiducialResult tag = getResultForTracking(result, targetId);

            if (tag == null) {
                return 0;
            }

            Pose3D tagPose = tag.getTargetPoseCameraSpace();

            if (tagPose == null) {
                return 0;
            }

            Position pos = tagPose.getPosition();
            Position inches = pos.toUnit(DistanceUnit.INCH);

            return Math.sqrt(inches.x * inches.x + inches.y * inches.y + inches.z * inches.z);
        }

        return 0;
    }

    public LLResultTypes.FiducialResult getResultForTracking(LLResult result, int targetId) {
        if (result.isValid() && !result.getFiducialResults().isEmpty()) {
            for (LLResultTypes.FiducialResult res : result.getFiducialResults()) {
                if (res.getFiducialId() == targetId) {
                    return res;
                }
            }
            return result.getFiducialResults().get(0);
        }
        return null;
    }

    public void initializeLimelight() {
        slimelight.setPollRateHz(100);
        slimelight.pipelineSwitch(0);
        slimelight.start();
    }

    public LLResult getResult() {
        return slimelight.getLatestResult();
    }

    public double update(TelemetryLogger telemetryLogger, int fiducialId) {
        LLResult result = getResult();

        distance = getDistance(result, fiducialId);
        double xOffset = getXOffset(result, fiducialId);
        double turningPower = trackAprilTag(result, fiducialId);

        tracking.turnMotor(turningPower);
        telemetryLogger.logLimelight(distance, xOffset, fiducialId);

        return distance;
    }

}

