package org.firstinspires.ftc.teamcode.helper.intefaces;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public interface Shooter {

    double DEADZONE = 250;
    double BASE_VELOCITY = 6000;
    double MAX_POWER = 0.625;
    double MIN_POWER = 0.475;
    double MIN_SERVO_POS = 0.1;
    double MAX_SERVO_POS = 0.6;
    double MIN_DISTANCE = 30;
    double MAX_DISTANCE = 115;
    boolean isShooterReady();
    double shoot(double distance);
    void stopShooter();
    void handleShoot(boolean shooterActive, double distance, Telemetry telemetry);

}
