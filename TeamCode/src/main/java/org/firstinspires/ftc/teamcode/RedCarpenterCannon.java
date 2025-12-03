package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.helper.LimelightHelper;
import org.firstinspires.ftc.teamcode.helper.Shooter;

import java.util.HashSet;


@TeleOp(name = "Red Tele Op")
public class RedCarpenterCannon extends LinearOpMode {


    // to do
    // central telemetry class
    // test red shooter
    // 

    private LimelightHelper slimelight;
    private Shooter cannon;
    final int redFiducialId = 24;


    @Override
    public void runOpMode() throws InterruptedException{
        // Define motors, make sure Id's match

        slimelight = new LimelightHelper(hardwareMap);
        cannon = new Shooter(hardwareMap);

        waitForStart();
        slimelight.initializeLimelight();


        if (isStopRequested()) return;

        boolean shooterActive = false;

        while (opModeIsActive()){

            slimelight.Run(telemetry, redFiducialId);
            double distance = slimelight.getDistance();


            if(gamepad1.a){
                shooterActive = true;
            }
            if(gamepad1.x){
                shooterActive = false;
            }

            cannon.handleShoot(shooterActive, distance, telemetry);

        }
    }



}
