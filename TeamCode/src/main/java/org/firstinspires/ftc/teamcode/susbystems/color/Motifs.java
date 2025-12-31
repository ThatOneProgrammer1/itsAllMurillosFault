package org.firstinspires.ftc.teamcode.susbystems.color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Motifs {

    public static Map<Integer, List<ColorDet.DetectedColor>> motifs = Map.of(

            21, Arrays.asList(
                    ColorDet.DetectedColor.PURPLE,
                    ColorDet.DetectedColor.PURPLE,
                    ColorDet.DetectedColor.GREEN
            ),

            22, Arrays.asList(
                    ColorDet.DetectedColor.GREEN,
                    ColorDet.DetectedColor.GREEN,
                    ColorDet.DetectedColor.PURPLE
            ),


            23, Arrays.asList(
                    ColorDet.DetectedColor.PURPLE,
                    ColorDet.DetectedColor.GREEN,
                    ColorDet.DetectedColor.PURPLE
            )

    );

    public static Map<Integer, List<ColorDet.DetectedColor>> getMotifs(){
        return motifs;
    }




}
