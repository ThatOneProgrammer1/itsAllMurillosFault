package org.firstinspires.ftc.teamcode.helper;

import org.firstinspires.ftc.robotcore.external.Telemetry;

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




}
