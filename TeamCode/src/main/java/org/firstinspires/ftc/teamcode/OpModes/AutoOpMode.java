package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.Subsystems.ScissorLift;
import org.firstinspires.ftc.teamcode.Subsystems.Vision;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

/**
 * FTC WIRES Autonomous Example
 */
@Autonomous(name = "FTC Wires Autonomous", group = "00-Autonomous", preselectTeleOp = "FTC Wires TeleOp")
public class AutoOpMode extends LinearOpMode{

    //Define and declare Robot Starting Locations
    public enum START_POSITION{
        BLUE_LEFT,
        BLUE_RIGHT,
        RED_LEFT,
        RED_RIGHT
    }
    public static START_POSITION startPosition;

    public Vision vision;
    public DriveTrain driveTrain;
    public ScissorLift scissorLift;

    @Override
    public void runOpMode() throws InterruptedException {
        /*Create your Subsystem Objects*/
        driveTrain = new DriveTrain(hardwareMap);
        vision = new Vision(hardwareMap);
        scissorLift = new ScissorLift();

        //Key Pay inputs to selecting Starting Position of robot
        selectStartingPosition();

        // Initiate Camera on Init.
        vision.activateVuforiaTensorFlow();

        //Build Autonomous trajectory to be used based on starting position selected
        buildAuto();
        driveTrain.getLocalizer().setPoseEstimate(initPose);
        scissorLift.scissorSetup();

        while (!isStopRequested() && !opModeIsActive()) {
            //Run Vuforia Tensor Flow and keep watching for the identifier in the Signal Cone.
            vision.runVuforiaTensorFlow();
            telemetry.clearAll();
            telemetry.addData("Start FTC Wires (ftcwires.org) Autonomous Mode adopted for Team","17430");
            telemetry.addData("---------------------------------------","");
            telemetry.addData("Selected Starting Position", startPosition);
            telemetry.addData("Vision identified Parking Location", vision.identifiedparkingLocation);
            telemetry.update();
        }

        //Game Play Button  is pressed
        if (opModeIsActive() && !isStopRequested()) {
            //Stop Vision process
            vision.deactivateVuforiaTensorFlow();

            //Build parking trajectory based on last detected target by vision
            buildParking();

            //run Autonomous trajectory
            runAutoAndParking();
        }

        //Trajectory is completed, display Parking complete
        parkingComplete();
    }

    //Initialize any other TrajectorySequences as desired
    TrajectorySequence trajectoryAuto, trajectoryParking ;

    //Initialize any other Pose2d's as desired
    Pose2d initPose; // Starting Pose
    Pose2d midWayPose;
    Pose2d pickConePose;
    Pose2d dropConePose0, dropConePose1, dropConePose2;
    Pose2d parkPose;

    //Set all position based on selected staring location and Build Autonomous Trajectory
    public void buildAuto() {
        switch (startPosition) {
            case BLUE_LEFT:
                initPose = new Pose2d(-54, 36, Math.toRadians(0)); //Starting pose
                midWayPose = new Pose2d(-12, 36, Math.toRadians(0)); //Choose the pose to move forward towards signal cone
                pickConePose = new Pose2d(-12, 55, Math.toRadians(90)); //Choose the pose to move to the stack of cones
                dropConePose0 = new Pose2d(-12, 12, Math.toRadians(225)); //Choose the pose to move to the stack of cones
                dropConePose1 = new Pose2d(-11, 12, Math.toRadians(225)); //Choose the pose to move to the stack of cones
                dropConePose2 = new Pose2d(-10, 12, Math.toRadians(225)); //Choose the pose to move to the stack of cones
                switch(vision.identifiedparkingLocation){
                    case 1: parkPose = new Pose2d(-12, 60, Math.toRadians(180)); break; // Location 1
                    case 2: parkPose = new Pose2d(-12, 36, Math.toRadians(180)); break; // Location 2
                    case 3: parkPose = new Pose2d(-12, 11, Math.toRadians(180)); break; // Location 3
                }
                break;
            case BLUE_RIGHT:
                initPose = new Pose2d(-54, -36, Math.toRadians(0));//Starting pose
                midWayPose = new Pose2d(-12, -36, Math.toRadians(0)); //Choose the pose to move forward towards signal cone
                pickConePose = new Pose2d(-12, -55, Math.toRadians(270)); //Choose the pose to move to the stack of cones
                dropConePose0 = new Pose2d(-12, -12, Math.toRadians(135)); //Choose the pose to move to the stack of cones
                dropConePose1 = new Pose2d(-11, -12, Math.toRadians(135)); //Choose the pose to move to the stack of cones
                dropConePose2 = new Pose2d(-10, -12, Math.toRadians(135)); //Choose the pose to move to the stack of cones
                switch(vision.identifiedparkingLocation){
                    case 1: parkPose = new Pose2d(-12, -11, Math.toRadians(180)); break; // Location 1
                    case 2: parkPose = new Pose2d(-12, -36, Math.toRadians(180)); break; // Location 2
                    case 3: parkPose = new Pose2d(-12, -60, Math.toRadians(180)); break; // Location 3
                }
                break;
            case RED_LEFT:
                initPose = new Pose2d(54, -36, Math.toRadians(180));//Starting pose
                midWayPose = new Pose2d(12, -36, Math.toRadians(180)); //Choose the pose to move forward towards signal cone
                pickConePose = new Pose2d(12, -55, Math.toRadians(270)); //Choose the pose to move to the stack of cones
                dropConePose0 = new Pose2d(12, -12, Math.toRadians(45)); //Choose the pose to move to the stack of cones
                dropConePose1 = new Pose2d(11, -12, Math.toRadians(45)); //Choose the pose to move to the stack of cones
                dropConePose2 = new Pose2d(10, -15, Math.toRadians(45)); //Choose the pose to move to the stack of cones
                switch(vision.identifiedparkingLocation){
                    case 1: parkPose = new Pose2d(12, -60, Math.toRadians(0)); break; // Location 1
                    case 2: parkPose = new Pose2d(12, -36, Math.toRadians(0)); break; // Location 2
                    case 3: parkPose = new Pose2d(12, -11, Math.toRadians(0)); break; // Location 3
                }
                break;
            case RED_RIGHT:
                initPose = new Pose2d(54, 36, Math.toRadians(180)); //Starting pose
                midWayPose = new Pose2d(12, 36, Math.toRadians(180)); //Choose the pose to move forward towards signal cone
                pickConePose = new Pose2d(12, 55, Math.toRadians(90)); //Choose the pose to move to the stack of cones
                dropConePose0 = new Pose2d(12, 12, Math.toRadians(315)); //Choose the pose to move to the stack of cones
                dropConePose1 = new Pose2d(11, 12, Math.toRadians(315)); //Choose the pose to move to the stack of cones
                dropConePose2 = new Pose2d(10, 12, Math.toRadians(315)); //Choose the pose to move to the stack of cones
                switch(vision.identifiedparkingLocation){
                    case 1: parkPose = new Pose2d(12, 11, Math.toRadians(0)); break; // Location 1
                    case 2: parkPose = new Pose2d(12, 36, Math.toRadians(0)); break; // Location 2
                    case 3: parkPose = new Pose2d(12, 60, Math.toRadians(0)); break; // Location 3
                }
                break;
        }

        //Drop Preloaded Cone, Pick 5 cones and park
        trajectoryAuto = driveTrain.trajectorySequenceBuilder(initPose)
                .lineToLinearHeading(midWayPose)
                .lineToLinearHeading(dropConePose0)
                .addDisplacementMarker(() -> {
                    dropCone(0); //Drop preloaded Cone
                })
                .lineToLinearHeading(midWayPose)
                .lineToLinearHeading(pickConePose)
                .addDisplacementMarker(() -> {
                    pickCone(1); //Pick top cone from stack
                })
                .lineToLinearHeading(midWayPose)
                .lineToLinearHeading(dropConePose1)
                .addDisplacementMarker(() -> {
                    dropCone(1); //Drop cone on junction
                })
                .lineToLinearHeading(midWayPose)
                .lineToLinearHeading(pickConePose)
                .addDisplacementMarker(() -> {
                    pickCone(2); //Pick second cone from stack
                })
                .lineToLinearHeading(midWayPose)
                .lineToLinearHeading(dropConePose2)
                .addDisplacementMarker(() -> {
                    dropCone(2); //Drop cone on junction
                })
                .lineToLinearHeading(midWayPose)
                .build();
    }

    //Build parking trajectory based on target detected by vision
    public void buildParking(){
        trajectoryParking = driveTrain.trajectorySequenceBuilder(midWayPose)
                .lineToLinearHeading(parkPose)
                .build();
    }

    //Run Auto trajectory and parking trajectory
    public void runAutoAndParking(){
        telemetry.setAutoClear(false);
        telemetry.addData("Running FTC Wires (ftcwires.org) Autonomous Mode adopted for Team:","17430");
        telemetry.addData("---------------------------------------","");
        telemetry.update();
        //Run the trajectory built for Auto and Parking
        driveTrain.followTrajectorySequence(trajectoryAuto);
        driveTrain.followTrajectorySequence(trajectoryParking);
    }

    //Write a method which is able to pick the cone from the stack depending on your subsystems
    public void pickCone(int coneCount) {

        if (coneCount == 1){
            telemetry.addData("Grabbed Cone: Stack", coneCount);
            //todo figure out what the lift positions are
            //scissorLift.liftToPosition(1234);
            //scissorLift.setBrainSpike(1);
        }
        else if (coneCount == 2){
            telemetry.addData("Grabbed Cone: Stack", coneCount);
            //todo figure out what the lift positions are
            //scissorLift.liftToPosition(1234);
            //scissorLift.setBrainSpike(1);
        }
        else if (coneCount == 3){
            telemetry.addData("Grabbed Cone: Stack", coneCount);
            //todo figure out what the lift positions are
            //scissorLift.liftToPosition(1234);
            //scissorLift.setBrainSpike(1);
        }

        else if (coneCount == 4){
            telemetry.addData("Grabbed Cone: Stack", coneCount);
            //todo figure out what the lift positions are
            //scissorLift.liftToPosition(1234);
            //scissorLift.setBrainSpike(1);
        }

        else {
            telemetry.addData("Dropped Cone: Stack", coneCount);
        }

        telemetry.update();
    }

    //Write a method which is able to drop the cone depending on your subsystems
    public void dropCone(int coneCount){
        /*TODO: Add code to drop cone on junction
        Since it drops fairly flat, we can just run the lift to the appropriate height in the "pickCone" step and
        only release here*/

        if (coneCount == 0) {
            scissorLift.setBrainSpike(1);
            telemetry.addData("Dropped Cone", "Pre-loaded");
        }
        else if (coneCount == 1){
            telemetry.addData("Dropped Cone: Stack", coneCount);
            //todo figure out what the lift positions are
            //scissorLift.liftToPosition(1234);
            //scissorLift.setBrainSpike(1);
        }
        else if (coneCount == 2){
            telemetry.addData("Dropped Cone: Stack", coneCount);
            //todo figure out what the lift positions are
            //scissorLift.liftToPosition(1234);
            //scissorLift.setBrainSpike(1);
        }
        else if (coneCount == 3){
            telemetry.addData("Dropped Cone: Stack", coneCount);
            //todo figure out what the lift positions are
            //scissorLift.liftToPosition(1234);
            //scissorLift.setBrainSpike(1);
        }

        else if (coneCount == 4){
            telemetry.addData("Dropped Cone: Stack", coneCount);
            //todo figure out what the lift positions are
            //scissorLift.liftToPosition(1234);
            //scissorLift.setBrainSpike(1);
        }

        else {
            telemetry.addData("Dropped Cone: Stack", coneCount);
        }
        telemetry.update();
    }

    public void parkingComplete(){
        telemetry.addData("Parked in Location", vision.identifiedparkingLocation);
        telemetry.update();
    }

    //Method to select starting position using X, Y, A, B buttons on gamepad
    public void selectStartingPosition() {
        telemetry.setAutoClear(true);
        telemetry.clearAll();
        //******select start pose*****
        while(!isStopRequested()){
            telemetry.addData("Initializing FTC Wires (ftcwires.org) Autonomous Mode adopted for Team:","17430");
            telemetry.addData("---------------------------------------","");
            telemetry.addData("Select Starting Position using XYAB Keys on gamepad 1:","");
            telemetry.addData("    Blue Left   ", "(X)");
            telemetry.addData("    Blue Right ", "(Y)");
            telemetry.addData("    Red Left    ", "(B)");
            telemetry.addData("    Red Right  ", "(A)");
            if(gamepad1.x){
                startPosition = START_POSITION.BLUE_LEFT;
                break;
            }
            if(gamepad1.y){
                startPosition = START_POSITION.BLUE_RIGHT;
                break;
            }
            if(gamepad1.b){
                startPosition = START_POSITION.RED_LEFT;
                break;
            }
            if(gamepad1.a){
                startPosition = START_POSITION.RED_RIGHT;
                break;
            }
            telemetry.update();
        }
        telemetry.clearAll();
    }
}

