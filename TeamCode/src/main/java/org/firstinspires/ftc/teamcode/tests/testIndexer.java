package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.indexer.indexing;
import org.firstinspires.ftc.teamcode.subsystems.other.TelemetryLogger;

@TeleOp(name = "testIndexer")
public class testIndexer extends LinearOpMode {

    private TelemetryLogger telemetryLogger;
    private indexing indexer = new indexing();


    @Override
    public void runOpMode() throws InterruptedException{
        telemetryLogger = new TelemetryLogger(telemetry);
        indexer.init(hardwareMap, telemetryLogger);
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
