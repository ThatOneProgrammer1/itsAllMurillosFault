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
    private Slimelight slimelight;
    private TelemetryLogger logger;
    private Cannon cannon;

    public enum PathState {
        DRIVE_STARTPOS_SHOOT_POS,
        SHOOT_STARTPOS_INTAKE_BEGIN_POS,
        INTAKE_STARTPOS_INTAKE_END_POS,
        INTAKE_STARTPOS_SHOOT_POS,
        SHOOT_STARTPOS_INTAKE2_POS,
        INTAKE2_BEGIN_STARTPOS_START_POS,
        INTAKE2_START_STARTPOS_END_POS
    }

    PathState pathState;

    // the position where the robot starts (facing blue goal)
    private final Pose startPose = new Pose(21.171528588098013, 122.15635939323221, Math.toRadians(135));

    // the position where we should shoot (on the triangle blue side)
    private final Pose shootPose = new Pose(60.65810968494749, 83.00583430571761, Math.toRadians(180));

    // the position where we would begin intake (right next to first set of balls on blue side)
    private final Pose intakeBeginPose = new Pose(33.43757292882147, 83.50991831971996, Math.toRadians(180));

    // position where intake will end (end of first set of balls)
    private final Pose intakeEndPose = new Pose(14.114352392065344, 83.50991831971996, Math.toRadians(180));

    // position where we shoot again
    private final Pose intakeToShootPose = new Pose(60.99416569428238, 83.17386231038508, Math.toRadians(135));

    // position straight line below where we shoot next to second set
    private final Pose shootToIntake2BeginPose = new Pose(60.82613768961494, 59.649941656942815, Math.toRadians(180));

    // in front of the second set of balls
    private final Pose intakeBeginToStartPose = new Pose(32.59743290548425, 59.649941656942815, Math.toRadians(180));

    // end of second set of balls
    private final Pose intakeStartToEndPose = new Pose(13.106184364060677, 59.649941656942815, Math.toRadians(180));


    // moves from start position to shooting position
    private PathChain driveStartPosShootPos;

    // moves from shooting position to beginning of intake for first set of balls
    private PathChain shootStartPosIntakePos;

    // moves from first intake set to the end intaking all first set balls
    private PathChain intakeStartPosIntakePos;

    // moves from end of intake to shooting position again
    private PathChain intakeStartPosShootPos;

    // lower on the straight line next to second set of balls
    private PathChain shootStartPosIntake2Pos;

    private PathChain intake2BeginToStartPos;

    private PathChain intake2StartToEndPos;

    public void buildPaths(){

        driveStartPosShootPos = follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();

        shootStartPosIntakePos = follower.pathBuilder()
                .addPath(new BezierLine(shootPose, intakeBeginPose))
                .setLinearHeadingInterpolation(shootPose.getHeading(), intakeBeginPose.getHeading())
                .build();

        intakeStartPosIntakePos = follower.pathBuilder()
                .addPath(new BezierLine(intakeBeginPose, intakeEndPose))
                .setLinearHeadingInterpolation(intakeBeginPose.getHeading(), intakeEndPose.getHeading())
                .build();

        intakeStartPosShootPos = follower.pathBuilder()
                .addPath(new BezierLine(intakeEndPose, intakeToShootPose))
                .setLinearHeadingInterpolation(intakeEndPose.getHeading(), intakeToShootPose.getHeading())
                .build();

        shootStartPosIntake2Pos = follower.pathBuilder()
                .addPath(new BezierLine(intakeToShootPose, shootToIntake2BeginPose))
                .setLinearHeadingInterpolation(intakeToShootPose.getHeading(), shootToIntake2BeginPose.getHeading())
                .build();

        intake2BeginToStartPos = follower.pathBuilder()
                .addPath(new BezierLine(shootToIntake2BeginPose, intakeBeginToStartPose))
                .setLinearHeadingInterpolation(shootToIntake2BeginPose.getHeading(), intakeBeginPose.getHeading())
                .build();

        intake2StartToEndPos = follower.pathBuilder()
                .addPath(new BezierLine(intakeBeginToStartPose, intakeStartToEndPose))
                .setLinearHeadingInterpolation(intakeBeginPose.getHeading(), intakeStartToEndPose.getHeading())
                .build();

    }

    public void statePathUpdate(){
        switch(pathState) {

            case DRIVE_STARTPOS_SHOOT_POS:
                follower.followPath(driveStartPosShootPos, true);
                setPathState(PathState.SHOOT_STARTPOS_INTAKE_BEGIN_POS);
                break;

            case SHOOT_STARTPOS_INTAKE_BEGIN_POS:
                if(!follower.isBusy()){
                    follower.followPath(shootStartPosIntakePos, true);
                    intake.startIntake();
                    setPathState(PathState.INTAKE_STARTPOS_INTAKE_END_POS);
                }
                break;

            case INTAKE_STARTPOS_INTAKE_END_POS:
                if(!follower.isBusy()){
                    follower.followPath(intakeStartPosIntakePos, true);
                    intake.stopIntake();
                    setPathState(PathState.INTAKE_STARTPOS_SHOOT_POS);
                }
            case INTAKE_STARTPOS_SHOOT_POS:
                if(!follower.isBusy()){

                    //shooting logic next

                    follower.followPath(intakeStartPosShootPos, true);

                    //rep;lace with shooting logic
                    cannon.setMotorPowers(1);
                    setPathState(PathState.SHOOT_STARTPOS_INTAKE2_POS);
                }
            case SHOOT_STARTPOS_INTAKE2_POS:
                if(!follower.isBusy() && pathTimer.getElapsedTimeSeconds() > 5){
                    cannon.setMotorPowers(0);
                    follower.followPath(shootStartPosIntake2Pos);
                    setPathState(PathState.INTAKE2_BEGIN_STARTPOS_START_POS);
                }
            case INTAKE2_BEGIN_STARTPOS_START_POS:
                if(!follower.isBusy()){
                    follower.followPath(intake2BeginToStartPos);
                    setPathState(PathState.INTAKE2_START_STARTPOS_END_POS);
                    intake.startIntake();
                }
            case INTAKE2_START_STARTPOS_END_POS:
                if(!follower.isBusy()){
                    follower.followPath(intake2StartToEndPos);
                    intake.stopIntake();
                    telemetry.addLine("Auto Finished");
                }


            default:
                telemetry.addLine("No state commanded");
        }
    }

    public void setPathState(PathState newState){
        pathState = newState;
        pathTimer.resetTimer();
    }

    @Override
    public void init() {
        pathState = PathState.DRIVE_STARTPOS_SHOOT_POS;

        pathTimer = new Timer();
        opModeTimer = new Timer();

        follower = Constants.createFollower(hardwareMap);
        intake = new Intake(hardwareMap);
        slimelight = new Slimelight(hardwareMap);
        logger = new TelemetryLogger(telemetry);
        cannon = new Cannon(hardwareMap);

        buildPaths();

        follower.setPose(startPose);
        setPathState(pathState);
    }

    @Override
    public void loop() {

        slimelight.update(logger, 24);

        follower.update();
        statePathUpdate();

        if(follower.getPose() != null){
            telemetry.addData("Y", follower.getPose().getY());
            telemetry.addData("X", follower.getPose().getX());
        }

        telemetry.addData("Path", pathState.toString());
        telemetry.addData("Path Timer", String.valueOf(pathTimer.getElapsedTime()));
        telemetry.update();
    }

}
