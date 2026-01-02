package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.susbystems.intake.Intake;
import org.firstinspires.ftc.teamcode.susbystems.limelight.Slimelight;
import org.firstinspires.ftc.teamcode.susbystems.misc.TelemetryLogger;
import org.firstinspires.ftc.teamcode.susbystems.shooter.Cannon;

@Autonomous
public class testpedro extends OpMode {

    private Follower follower;
    private Timer pathTimer, opModeTimer;
    private Intake intake;

    public enum PathState {
        DRIVE_TO_SHOOT,
        DRIVE_TO_INTAKE_1,
        DRIVE_BACK_TO_SHOOT,
        DRIVE_TO_INTAKE_2,
        STAY_AT_INTAKE_2,
        FINISHED
    }

    PathState pathState;
    Pose pose;

    private final Pose startPose = new Pose(21.17, 122.15, Math.toRadians(180));
    private final Pose shootPose = new Pose(60.65, 83.00, Math.toRadians(135));
    private final Pose intake1Pose = new Pose(33.43, 83.50, Math.toRadians(180));
    private final Pose intake2Pose = new Pose(60.82, 59.64, Math.toRadians(180));

    private PathChain toShoot, toIntake1, backToShoot, toIntake2;

    public void buildPaths(){
        // Start -> Shoot
        toShoot = follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();

        // Shoot -> Intake 1
        toIntake1 = follower.pathBuilder()
                .addPath(new BezierLine(shootPose, intake1Pose))
                .setLinearHeadingInterpolation(shootPose.getHeading(), intake1Pose.getHeading())
                .build();

        // Intake 1 -> Shoot
        backToShoot = follower.pathBuilder()
                .addPath(new BezierLine(intake1Pose, shootPose))
                .setLinearHeadingInterpolation(intake1Pose.getHeading(), shootPose.getHeading())
                .build();

        toIntake2 = follower.pathBuilder()
                .addPath(new BezierLine(shootPose, intake2Pose))
                .setLinearHeadingInterpolation(shootPose.getHeading(), intake2Pose.getHeading())
                .build();
    }

    public void statePathUpdate() {

        switch(pathState) {
            case DRIVE_TO_SHOOT:
                follower.followPath(toShoot, true);
                setPathState(PathState.DRIVE_TO_INTAKE_1);
                break;

            case DRIVE_TO_INTAKE_1:
                if(!follower.isBusy()){
                    intake.startIntake();
                    follower.followPath(toIntake1, true);
                    setPathState(PathState.DRIVE_BACK_TO_SHOOT);
                }
                break;

            case DRIVE_BACK_TO_SHOOT:
                if(!follower.isBusy()){
                    intake.stopIntake();
                    follower.followPath(backToShoot, true);
                    setPathState(PathState.DRIVE_TO_INTAKE_2);
                }
                break;

            case DRIVE_TO_INTAKE_2:
                if(!follower.isBusy()){
                    if(pathTimer.getElapsedTimeSeconds() > 2.0) {
                        intake.startIntake();
                        follower.followPath(toIntake2, true);
                        setPathState(PathState.STAY_AT_INTAKE_2);
                    }
                }
                break;

            case STAY_AT_INTAKE_2:
                if(!follower.isBusy()){
                    intake.stopIntake();
                    setPathState(PathState.FINISHED);
                }
                break;

            case FINISHED:
                break;
        }
    }

    public void setPathState(PathState newState){
        pathState = newState;
        pathTimer.resetTimer();
    }

    @Override
    public void init() {
        pathTimer = new Timer();
        opModeTimer = new Timer();

        follower = Constants.createFollower(hardwareMap);
        intake = new Intake(hardwareMap);

        buildPaths();
        follower.setPose(startPose);
        setPathState(PathState.DRIVE_TO_SHOOT);
    }

    @Override
    public void loop() {

        follower.update();
        statePathUpdate();

        pose = follower.getPose();
        double disToPos = follower.getTranslationalError().getMagnitude();
        double headingError = follower.getHeading();



        telemetry.addData("Path State", pathState);
        telemetry.addData("X", pose.getX());
        telemetry.addData("Y", pose.getY());
        telemetry.addData("Heading (deg)", Math.toDegrees(follower.getHeading()));
        telemetry.addData("Distance", disToPos);
        telemetry.addData("Heading Error", headingError);
        telemetry.update();
    }
}