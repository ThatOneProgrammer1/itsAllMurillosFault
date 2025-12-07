package org.firstinspires.ftc.teamcode.susbystems.color;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

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

    public DetectedColor detectColor(){

        android.graphics.Color.RGBToHSV(
                sensor.red() * 8,
                sensor.green() * 8,
                sensor.blue() * 8,
                hsv
        );

        float hue = hsv[0];

        if (hue > 85 && hue < 200) {
            return DetectedColor.GREEN;
        }
        else if (hue > 200 && hue < 300){
            return DetectedColor.PURPLE;
        }

        return DetectedColor.UNKNOWN;

    }





}
