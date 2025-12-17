package org.firstinspires.ftc.teamcode.susbystems.color;

import com.qualcomm.robotcore.hardware.ColorSensor;

public class SensorPair<ColorSensorA, ColorSensorB> {

    public final ColorSensorA first;
    public final ColorSensorB second;
    public SensorPair(ColorSensorA sA, ColorSensorB sB){
        this.first = sA;
        this.second = sB;

    }
}
