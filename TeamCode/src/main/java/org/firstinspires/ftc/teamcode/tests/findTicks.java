package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.turret.Turret;

@TeleOp(name = "findTicks")
public class findTicks extends LinearOpMode {
    Turret turret;

    @Override
    public void runOpMode() throws InterruptedException {
        turret = new Turret(hardwareMap);







        waitForStart();

        if (isStopRequested()) return;
        while (opModeIsActive()){
            turret.getPosition();
            int pos = turret.getPosition();
            telemetry.addData("ticks", pos);

            telemetry.update();
        }
    }
}
