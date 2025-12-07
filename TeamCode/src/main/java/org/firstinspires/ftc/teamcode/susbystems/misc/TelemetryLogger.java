package org.firstinspires.ftc.teamcode.susbystems.misc;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.susbystems.color.ColorDet;

public class TelemetryLogger {

    private Telemetry telemetry;
    public TelemetryLogger(Telemetry telemetry){
        this.telemetry = telemetry;
    }

    public void logLimelight(double distance,
                             double xOffset,
                             double fiducialId,
                             double yOffset)
    {
        telemetry.addData("Distance", distance);
        telemetry.addData("X offset", xOffset);
        telemetry.addData("Fiducial ID", fiducialId);
        telemetry.addData("Y offset", yOffset);
        telemetry.update();
    }

    public void logShootPower(double power){
        telemetry.addData("Shoot Power", power);
        telemetry.update();
    }

    public void logColor(ColorDet.DetectedColor color){
        String name = "Unknown";

        if(color == ColorDet.DetectedColor.GREEN){
            name = "Green";
        }
        else if(color == ColorDet.DetectedColor.PURPLE){
            name = "Purple";
        }

        telemetry.addData("Detected Color", name);
        telemetry.update();


    }




}
