package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous
public class deletetestpedroauto extends OpMode {

    private Follower follower;

    private final Pose startPose = new Pose(21.171528588098013, 122.15635939323221, Math.toRadians(135));

    // the position where we should shoot (on the triangle blue side)
    private final Pose shootPose = new Pose(60.65810968494749, 83.00583430571761, Math.toRadians(180));

    private PathChain testChain;

    public void buildPaths(){

        testChain = follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();

    }

    @Override
    public void init() {

        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setPose(startPose);

    }

    boolean ranAuto = false;

    @Override
    public void loop() {
        follower.update();
        if(!follower.isBusy() && !ranAuto){
            follower.followPath(testChain, true);
            ranAuto = true;
        }
        telemetry.addData("X", String.valueOf(follower.getPose().getX()));
        telemetry.addData("Y", String.valueOf(follower.getPose().getY()));
        telemetry.addData("Heading", String.valueOf(follower.getPose().getHeading()));

        telemetry.update();
    }
}
