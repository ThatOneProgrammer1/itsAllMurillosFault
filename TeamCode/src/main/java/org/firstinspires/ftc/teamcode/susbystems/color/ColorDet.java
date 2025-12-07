package org.firstinspires.ftc.teamcode.susbystems.color;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.susbystems.misc.TelemetryLogger;

import java.util.ArrayList;
import java.util.List;

public class ColorDet {

    private ColorSensor sensor;

    float[] hsv = new float[3];

    public ColorDet(HardwareMap hardwareMap){
        sensor = hardwareMap.get(ColorSensor.class, "color1");
    }
    public enum DetectedColor{
        PURPLE,
        GREEN,
        UNKNOWN
    }

    List<DetectedColor> heldColors = new ArrayList<>();

    public DetectedColor detectColor(TelemetryLogger telemetryLogger){

        DetectedColor color = DetectedColor.UNKNOWN;

        android.graphics.Color.RGBToHSV(
                sensor.red() * 8,
                sensor.green() * 8,
                sensor.blue() * 8,
                hsv
        );

        float hue = hsv[0];

        if (hue > 85 && hue < 200) {
            color = DetectedColor.GREEN;
        }
        else if (hue > 200 && hue < 300){
            color = DetectedColor.PURPLE;
        }

        telemetryLogger.logColor(color);

        return color;

    }





}
