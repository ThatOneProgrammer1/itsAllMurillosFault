package org.firstinspires.ftc.teamcode.subsystems.turret;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Turret {

    private DcMotorEx turret;


    public Turret(HardwareMap hardwareMap){

        turret = hardwareMap.get(DcMotorEx.class, "turner");
        turret.setDirection(DcMotorSimple.Direction.REVERSE);
        turret.setTargetPosition(0);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setVelocityPIDFCoefficients(12, 0.5, 0, 1/2000.);
        turret.setPositionPIDFCoefficients(38);

    }

    public double ticksPerDegree = 147/90.0;

    public int getPosition() {
        return turret.getCurrentPosition();
    }

    private boolean isWrapping = false;
    public int targetTicks;
    private double maxTicks = 235;
    private double minTicks = -150;
    private int ticksPerRotation = 588;

    public void setTurretAngle(double tx, boolean canSee, double spinRate, int manualTicks) {
        if (!canSee){
            if (manualTicks < minTicks) {
                manualTicks += ticksPerRotation;
            } else if (manualTicks > maxTicks) {
                manualTicks -= ticksPerRotation;
            }
            targetTicks = manualTicks;
            turret.setTargetPosition(manualTicks);
            turret.setPower(1.0);
            return;
        }

        double error = turret.getCurrentPosition() - targetTicks;
        if (isWrapping && Math.abs(error) > 30*ticksPerDegree) {
            turret.setTargetPosition(targetTicks);
            turret.setPower(1.0);
            return;
        }
        else if (isWrapping && Math.abs(error) > 10 * ticksPerDegree && !canSee) {
            turret.setTargetPosition(targetTicks);
            turret.setPower(1.0);
            return;
        }
        isWrapping  = false;
        targetTicks = turret.getCurrentPosition() + (int) (tx * ticksPerDegree);
        if (targetTicks > maxTicks){
            targetTicks = targetTicks - ticksPerRotation;
            isWrapping = true;
        } else if (targetTicks < minTicks) {
            targetTicks = targetTicks + ticksPerRotation;
            isWrapping = true;
        }
        turret.setTargetPosition(targetTicks);
        turret.setPower(1.0);
    }
}
