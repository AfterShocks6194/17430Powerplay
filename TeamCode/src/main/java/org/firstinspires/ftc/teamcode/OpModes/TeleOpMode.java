package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.Subsystems.ScissorLift;

/**
 * FTC WIRES TeleOp Example
 *
 */
@TeleOp(name = "FTC Wires TeleOp", group = "00-Teleop")
public class TeleOpMode extends LinearOpMode {

    public DriveTrain driveTrain;
    public ScissorLift scissorLift;

    @Override
    /*
     * Constructor for passing all the subsystems in order to make the subsystem be able to use
     * and work/be active
     */
    public void runOpMode() throws InterruptedException {

        /* Create Subsystem Objects*/
        driveTrain = new DriveTrain(hardwareMap);
        scissorLift = new ScissorLift();
        scissorLift.scissorSetup();

        telemetry.clearAll();
        telemetry.addData("Running FTC Wires TeleOp adopted for Team","17430");
        telemetry.update();
        /* Wait for Start or Stop Button to be pressed */
        waitForStart();

        /*If Start is pressed, enter loop and exit only when Stop is pressed */
        while (!isStopRequested()) {
            while (opModeIsActive()) {
                driveTrain.driveType = DriveTrain.DriveType.ROBOT_CENTRIC;
                //TODO check out robot centric vs field centric
//                driveTrain.driveType = DriveTrain.DriveType.FIELD_CENTRIC;
                driveTrain.gamepadInput = new Vector2d(-gamepad1.left_stick_y, -gamepad1.left_stick_x);
                driveTrain.gamepadInputTurn = -gamepad1.right_stick_x;
                driveTrain.driveTrainPointFieldModes();
            }
        }
    }
}
