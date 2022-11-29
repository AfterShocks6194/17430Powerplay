package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

@Config
public class ScissorLift {

    // TODO Check Motor Names in Robot Setup
    public DcMotorEx lift;
    public Servo brainSpike;

public void resetLiftEncoder(){
    lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
}

    public void liftToPosition(int position) {
        lift.setTargetPosition(position);
        lift.setPower(1);
    }

    public void setBrainSpike(double set) {
        brainSpike.setPosition(set);
    }

    public void setMode(DcMotor.RunMode runMode) {
        lift.setMode(runMode);
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        lift.setZeroPowerBehavior(zeroPowerBehavior);
    }

    public void scissorSetup(){
    setMode(DcMotor.RunMode.RUN_TO_POSITION);
    setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}