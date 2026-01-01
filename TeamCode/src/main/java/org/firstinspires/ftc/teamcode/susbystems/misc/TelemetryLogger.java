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
    }

    public void logShootPower(double power){
        telemetry.addData("Shoot Power", power);
    }

    public void logState(String state) {
        telemetry.addData("Index state", state);
    }

    public void log(String key, String val){
        telemetry.addData(key, val);
    }




}
