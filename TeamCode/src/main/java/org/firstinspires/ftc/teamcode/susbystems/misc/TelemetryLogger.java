package org.firstinspires.ftc.teamcode.susbystems.misc;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TelemetryLogger {

    private Telemetry telemetry;
    public TelemetryLogger(Telemetry telemetry){
        this.telemetry = telemetry;
    }

    public void logLimelight(double distance,
                             double xOffset,
                             double fiducialId)
    {
        telemetry.addData("Distance", distance);
        telemetry.addData("X offset", xOffset);
        telemetry.addData("Fiducial ID", fiducialId);
        telemetry.update();
    }

    public void logShootPower(double power){
        telemetry.addData("Shoot Power", power);
        telemetry.update();
    }

    public void log(String name, String data){
        telemetry.addData(name, data);
        telemetry.update();
    }




}
