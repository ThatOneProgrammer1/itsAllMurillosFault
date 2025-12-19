package org.firstinspires.ftc.teamcode.susbystems.color;

public class SensorPair<ColorSensorA, ColorSensorB> {

    public final ColorSensorA first;
    public final ColorSensorB second;
    public SensorPair(ColorSensorA sA, ColorSensorB sB){
        this.first = sA;
        this.second = sB;

    }
}
