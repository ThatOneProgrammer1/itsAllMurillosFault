package org.firstinspires.ftc.teamcode.susbystems.color;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ColorDet {

    // mark sensors as used after shooting

    public enum DetectedColor{
        PURPLE,
        GREEN,
        UNKNOWN
    }

    public ColorDet(HardwareMap hardwareMap){
        firstBallSensor1 = hardwareMap.get(ColorSensor.class, "fbs1");
        firstBallSensor2 = hardwareMap.get(ColorSensor.class, "fbs2");
        secondBallSensor1 = hardwareMap.get(ColorSensor.class, "sbs1");
        secondBallSensor2 = hardwareMap.get(ColorSensor.class, "sbs2");
        thirdBallSensor1 = hardwareMap.get(ColorSensor.class, "tbs1");
        thirdBallSensor2 = hardwareMap.get(ColorSensor.class, "tbs2");

        ballSensorsOne = new SensorPair<>(firstBallSensor1, firstBallSensor2);
        ballSensorsTwo = new SensorPair<>(secondBallSensor1, secondBallSensor2);
        ballSensorsThree = new SensorPair<>(thirdBallSensor1, thirdBallSensor2);

        sensorPairs = Arrays.asList(
                ballSensorsOne,
                ballSensorsTwo,
                ballSensorsThree
        );

        sensorServoMap = Map.of(
                ballSensorsOne, 0,
                ballSensorsTwo, 1,
                ballSensorsThree, 2
        );
    }

    public Integer trackedMotifId = 0;
    public List<DetectedColor> trackedMotifColors = new ArrayList<>();

    private ColorSensor firstBallSensor1;
    private ColorSensor firstBallSensor2;

    private ColorSensor secondBallSensor1;
    private ColorSensor secondBallSensor2;

    private ColorSensor thirdBallSensor1;
    private ColorSensor thirdBallSensor2;

    float[] hsv = new float[3];

    SensorPair<ColorSensor, ColorSensor> ballSensorsOne;
    SensorPair<ColorSensor, ColorSensor> ballSensorsTwo;
    SensorPair<ColorSensor, ColorSensor> ballSensorsThree;

    List<SensorPair<ColorSensor, ColorSensor>> sensorPairs;
    Map<SensorPair<ColorSensor, ColorSensor>, Integer> sensorServoMap;

    private int currentBallIndex = 0;

    public void getMotif(int id){
        if(Motifs.getMotifs().get(id) != null) {
            trackedMotifId = id;
            trackedMotifColors = Motifs.getMotifs().get(id);
        }
    }

    public boolean ballColorsMatchMotif(){

        Map<DetectedColor, Integer> heldMap = new HashMap<>();
        Map<DetectedColor, Integer> requiredMap = new HashMap<>();

        for(SensorPair<ColorSensor, ColorSensor> pair: sensorPairs){
            DetectedColor color = detectColor(pair);
            heldMap.put(color, heldMap.getOrDefault(color, 0) + 1);
        }
        for(DetectedColor color: trackedMotifColors){
            requiredMap.put(color, requiredMap.getOrDefault(color, 0) + 1);
        }

        return heldMap.equals(requiredMap);
    }

    // implement later
    public void shootInSequence(){
        if(!ballColorsMatchMotif()){
            return;
        }
    }


    public int shootNextBall(){
        if (currentBallIndex >= trackedMotifColors.size()) {
            return -1;
        }

        DetectedColor colorRequired = trackedMotifColors.get(currentBallIndex);

        for(SensorPair pair: sensorPairs){
            if(detectColor(pair) == colorRequired){
                currentBallIndex = Math.min(trackedMotifColors.size(), currentBallIndex+1);
                return sensorServoMap.get(pair);
            }
        }
        return -1;
    }


    public DetectedColor detectColor(SensorPair<ColorSensor, ColorSensor> sensorPair){
        
        double red = (double) (sensorPair.first.red() + sensorPair.second.red()) / 2;
        double blue = (double) (sensorPair.first.blue() + sensorPair.second.blue()) / 2;
        double green = (double) (sensorPair.first.green() + sensorPair.second.green()) / 2;

        if(red + blue > green){
            return DetectedColor.GREEN;
        }
        else{
            return DetectedColor.PURPLE;
        }
    }





}
