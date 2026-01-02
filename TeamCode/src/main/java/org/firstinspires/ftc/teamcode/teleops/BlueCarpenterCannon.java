package org.firstinspires.ftc.teamcode.teleops;

import com.bylazar.field.FieldManager;
import com.bylazar.field.PanelsField;
import com.bylazar.field.Style;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.PoseHistory;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.pedroPathing.Tuning;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.vroomvroom;
import org.firstinspires.ftc.teamcode.subsystems.turret.Turret;
import org.firstinspires.ftc.teamcode.subsystems.other.TelemetryLogger;
import org.firstinspires.ftc.teamcode.subsystems.turret.Cannon;
import org.firstinspires.ftc.teamcode.subsystems.indexer.indexing;

import java.util.List;


@TeleOp(name = "Blue Tele Op")
public class BlueCarpenterCannon extends LinearOpMode {
    vroomvroom drive = new vroomvroom();

    double forward, strafe, rotate;
    public DcMotorEx intake;
    public double intakePower;

    // test if works
    // fix distance power scaling method
    // separate classes
    // start drivetrain

    private TelemetryLogger telemetryLogger;
    private Turret turret;
    private Limelight3A slimelight;
    private Cannon cannon;
    private indexing indexer = new indexing();
    private Follower follower;
    final int blueFiducialId = 20;
    Pose goalPose = new Pose(-58.3727, 55.6425); // Blue goal pose
    Pose startPose = new Pose(60, 35);

    @Override
    public void runOpMode() throws InterruptedException {
        drive.init(hardwareMap);

        intake = hardwareMap.get(DcMotorEx.class, "intake");
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        // Define motors, make sure Id's match

        slimelight = hardwareMap.get(Limelight3A.class, "slimelight");
        cannon = new Cannon(hardwareMap);
        telemetryLogger = new TelemetryLogger(telemetry);
        indexer.init(hardwareMap, telemetryLogger);
        turret = new Turret(hardwareMap);

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        follower.setPose(startPose);
        double distance = 0.0;
        double xOffset = 0.0;
        double yOffset = 0.0;
        double shootPower = 0.0;
        double servoPos = 0.0;

        Pose botpose = new Pose();
        double ty = 0;
        double tx = 0;


        waitForStart();

        if (isStopRequested()) return;

        boolean canSee = false;
        boolean shooterActive = false;
        double TICKS_PER_DEG = 147/90.0;;
        double MAX_ANGLE = 270;
        double MIN_ANGLE = -270;
        double turretTarget = 0.0;
        double kP = 4;

        while (opModeIsActive()) {
            forward = -gamepad1.left_stick_y;
            strafe = gamepad1.left_stick_x;
            rotate = gamepad1.right_stick_x;

            indexer.update();

            LLResult result = slimelight.getLatestResult();
            LLStatus status = slimelight.getStatus();

            telemetry.addData("Name", "%s",
                    status.getName());
            telemetry.addData("LL", "Temp: %.1fC, CPU: %.1f%%, FPS: %d",
                    status.getTemp(), status.getCpu(), (int) status.getFps());

            if (result.isValid()) {
                // Access general information
                Pose3D pose = result.getBotpose();
                Position position = pose.getPosition().toUnit(DistanceUnit.INCH);
                botpose = new Pose(position.x, position.y, pose.getOrientation().getYaw());
                telemetry.addData("tx", result.getTx());
                telemetry.addData("txnc", result.getTxNC());
                telemetry.addData("ty", result.getTy());
                telemetry.addData("tync", result.getTyNC());
                telemetry.addData("Botpose", botpose.toString());

                // Access fiducial results
                List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
                for (LLResultTypes.FiducialResult fr : fiducialResults) {
                    telemetry.addData("Fiducial", "ID: %d, Family: %s, X: %.2f, Y: %.2f",
                            fr.getFiducialId(), fr.getFamily(), fr.getTargetXDegrees(), fr.getTargetYDegrees());
                }
            } else {
                telemetry.addData("Limelight", "No data available");
            }

            List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
            for (LLResultTypes.FiducialResult fr : fiducialResults) {
                if (fr.getFiducialId() == blueFiducialId) {
                    ty = result.getTy();
                    tx = result.getTx();
                    canSee = true;
                }
            }
            follower.update();
            distance = goalPose.distanceFrom(follower.getPose());

            if(gamepad1.aWasPressed()){
                intakePower = 0;
                shooterActive = true;
            }
            if(gamepad1.bWasPressed()){
                intakePower = 1;
            }
            if(gamepad1.xWasPressed()){
                intakePower = 0;
                shooterActive = false;
            }
            if(gamepad1.yWasPressed()){
                intakePower = 0.8;
            }
            if(gamepad1.startWasPressed()){
                drive.resetPos();
            }
            if(gamepad1.rightBumperWasPressed()){
                indexer.startShooting();
            }

            telemetry.addData("Turret Target", turret.targetTicks);
            telemetry.addData("Turret Current", turret.getPosition());


            if(shooterActive){
                cannon.shoot(distance);
            }
            else if (!shooterActive){
                cannon.stopShooter();
            }

            cannon.handleShoot(shooterActive, distance, telemetryLogger);

            int angle = getTurretTargetFromPose(follower.getPose());
            turret.setTurretAngle(tx, canSee, 0.0, angle);

            Drawing.drawDebug(follower);
            intake.setPower(intakePower);

            drive.driveFieldRelative(forward, strafe, rotate);
            telemetry.addData("forward", forward);
            telemetry.addData("strafe", strafe);
            telemetry.addData("rotate", rotate);
            telemetry.update();
        }

    }

    private double atanAngle = 0;
    private int getTurretTargetFromPose(Pose robotPose) {
        return getTurretTargetFromPose(robotPose, goalPose);
    }



    public int getTurretTargetFromPose(Pose robotPose, Pose targetPose) {
        atanAngle = Math.toDegrees(
                Math.atan2(
                        targetPose.getY() - robotPose.getY(),
                        targetPose.getX() - robotPose.getX()
                )
        );

        telemetry.addData("Atan angle deg", atanAngle);
        double otherAtanAngle = Math.toDegrees(robotPose.getHeading()) - atanAngle - 90;
        return (int)(turret.ticksPerDegree * otherAtanAngle);
    }
}

class Drawing {
    public static final double ROBOT_RADIUS = 9; // woah
    private static final FieldManager panelsField = PanelsField.INSTANCE.getField();

    private static final Style robotLook = new Style(
            "", "#3F51B5", 0.75
    );
    private static final Style historyLook = new Style(
            "", "#4CAF50", 0.75
    );

    /**
     * This prepares Panels Field for using Pedro Offsets
     */
    public static void init() {
        panelsField.setOffsets(PanelsField.INSTANCE.getPresets().getPEDRO_PATHING());
    }

    /**
     * This draws everything that will be used in the Follower's telemetryDebug() method. This takes
     * a Follower as an input, so an instance of the DashbaordDrawingHandler class is not needed.
     *
     * @param follower Pedro Follower instance.
     */
    public static void drawDebug(Follower follower) {
        if (follower.getCurrentPath() != null) {
            drawPath(follower.getCurrentPath(), robotLook);
            Pose closestPoint = follower.getPointFromPath(follower.getCurrentPath().getClosestPointTValue());
            drawRobot(new Pose(closestPoint.getX(), closestPoint.getY(), follower.getCurrentPath().getHeadingGoal(follower.getCurrentPath().getClosestPointTValue())), robotLook);
        }
        drawPoseHistory(follower.getPoseHistory(), historyLook);
        drawRobot(follower.getPose(), historyLook);

        sendPacket();
    }

    /**
     * This draws a robot at a specified Pose with a specified
     * look. The heading is represented as a line.
     *
     * @param pose  the Pose to draw the robot at
     * @param style the parameters used to draw the robot with
     */
    public static void drawRobot(Pose pose, Style style) {
        if (pose == null || Double.isNaN(pose.getX()) || Double.isNaN(pose.getY()) || Double.isNaN(pose.getHeading())) {
            return;
        }

        panelsField.setStyle(style);
        panelsField.moveCursor(pose.getX(), pose.getY());
        panelsField.circle(ROBOT_RADIUS);

        Vector v = pose.getHeadingAsUnitVector();
        v.setMagnitude(v.getMagnitude() * ROBOT_RADIUS);
        double x1 = pose.getX() + v.getXComponent() / 2, y1 = pose.getY() + v.getYComponent() / 2;
        double x2 = pose.getX() + v.getXComponent(), y2 = pose.getY() + v.getYComponent();

        panelsField.setStyle(style);
        panelsField.moveCursor(x1, y1);
        panelsField.line(x2, y2);
    }

    /**
     * This draws a robot at a specified Pose. The heading is represented as a line.
     *
     * @param pose the Pose to draw the robot at
     */
    public static void drawRobot(Pose pose) {
        drawRobot(pose, robotLook);
    }

    /**
     * This draws a Path with a specified look.
     *
     * @param path  the Path to draw
     * @param style the parameters used to draw the Path with
     */
    public static void drawPath(Path path, Style style) {
        double[][] points = path.getPanelsDrawingPoints();

        for (int i = 0; i < points[0].length; i++) {
            for (int j = 0; j < points.length; j++) {
                if (Double.isNaN(points[j][i])) {
                    points[j][i] = 0;
                }
            }
        }

        panelsField.setStyle(style);
        panelsField.moveCursor(points[0][0], points[0][1]);
        panelsField.line(points[1][0], points[1][1]);
    }

    /**
     * This draws all the Paths in a PathChain with a
     * specified look.
     *
     * @param pathChain the PathChain to draw
     * @param style     the parameters used to draw the PathChain with
     */
    public static void drawPath(PathChain pathChain, Style style) {
        for (int i = 0; i < pathChain.size(); i++) {
            drawPath(pathChain.getPath(i), style);
        }
    }

    /**
     * This draws the pose history of the robot.
     *
     * @param poseTracker the PoseHistory to get the pose history from
     * @param style       the parameters used to draw the pose history with
     */
    public static void drawPoseHistory(PoseHistory poseTracker, Style style) {
        panelsField.setStyle(style);

        int size = poseTracker.getXPositionsArray().length;
        for (int i = 0; i < size - 1; i++) {

            panelsField.moveCursor(poseTracker.getXPositionsArray()[i], poseTracker.getYPositionsArray()[i]);
            panelsField.line(poseTracker.getXPositionsArray()[i + 1], poseTracker.getYPositionsArray()[i + 1]);
        }
    }

    /**
     * This draws the pose history of the robot.
     *
     * @param poseTracker the PoseHistory to get the pose history from
     */
    public static void drawPoseHistory(PoseHistory poseTracker) {
        drawPoseHistory(poseTracker, historyLook);
    }

    /**
     * This tries to send the current packet to FTControl Panels.
     */
    public static void sendPacket() {
        panelsField.update();
    }
}