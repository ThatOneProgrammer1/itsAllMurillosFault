package org.firstinspires.ftc.teamcode.susbystems.color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Motifs {

    List<ColorDet.DetectedColor> heldColors = new ArrayList<>();

    public List<ColorDet.DetectedColor> getHeldColors(){
        return heldColors;
    }

    public void addColor(ColorDet.DetectedColor color){
        heldColors.add(color);
    }
    Map<Integer, List<ColorDet.DetectedColor>> motifs = Map.of(

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


    public void organizeColorShoot(int motifId){
        List<ColorDet.DetectedColor> colorOrder = motifs.get(motifId);

        // continue this after intake design
    }


}
