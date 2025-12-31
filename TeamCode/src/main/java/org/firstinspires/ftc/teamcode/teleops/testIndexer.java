package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.indexer.indexing;

@TeleOp(name = "testIndexer")
public class testIndexer extends LinearOpMode {

    private indexing indexer = new indexing();


    @Override
    public void runOpMode() throws InterruptedException{
        waitForStart();
        if (isStopRequested()) return;

        while(opModeIsActive()){

            indexer.update();
            if (gamepad1.rightBumperWasPressed()) {
                indexer.startShooting();
            }

        }
    }
}
