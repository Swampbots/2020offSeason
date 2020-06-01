package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.OffSeasonHardware;

@Autonomous(name = "Test: Robot Drive Square", group = "Testing")
public class TestRobotDriveSquare extends LinearOpMode {

    // Hardware
    OffSeasonHardware hardware = new OffSeasonHardware();
    private final boolean INIT_IMU = true;

    // Method-specific
    private final double DRIVE_SPEED = 0.8; // Default drive speed
    private final double MAX_DRIVE_SPEED = Math.min(DRIVE_SPEED + 0.1, 1.0);
    private final double COUNTS_PER_INCH_EMPIRICAL = 1000 / 24.0;    // Determined by testing (1000 counts / 24.0 inches)
    private final double K_P = 0.01;   // Proportional coefficient for gyro-controlled driving

    private final double DRIVE_TIMEOUT = 10.0;
    private final double ADJUST_TIMEOUT = 2.0;

    @Override
    public void runOpMode() {
        telemetry.addLine("Initializing");
        telemetry.update();

        hardware.init(hardwareMap, INIT_IMU);

        telemetry.addLine("Ready");
        telemetry.update();
        waitForStart();

        telemetry.addLine("Running");
        telemetry.update();

        driveInchesGyroNoStop(60.0, DRIVE_SPEED, 0, DRIVE_TIMEOUT);
        driveInchesGyro(72.0, DRIVE_SPEED, -90, DRIVE_TIMEOUT);
        driveInchesGyro(60.0, 0, -90, ADJUST_TIMEOUT);

        while(opModeIsActive()) {
            telemetry.addLine("Finished");
            telemetry.update();
        }
    }

    private void driveInchesGyro(double inches, double speed, double targetHeading, double timeout) {
        driveEncoderCountsGyro((int)(inches * COUNTS_PER_INCH_EMPIRICAL), speed, targetHeading, timeout);
    }
    private void driveInchesGyroNoStop(double inches, double speed, double targetHeading, double timeout) {
        driveEncoderCountsGyroNoStop((int)(inches * COUNTS_PER_INCH_EMPIRICAL), speed, targetHeading, timeout);
    }

    private void driveEncoderCountsGyro(int counts, double speed, double targetHeading, double timeout) {
        hardware.setDriveCounts(counts);

        hardware.frontLeft.setMode  (DcMotor.RunMode.RUN_TO_POSITION);
        hardware.frontRight.setMode (DcMotor.RunMode.RUN_TO_POSITION);
        hardware.rearLeft.setMode   (DcMotor.RunMode.RUN_TO_POSITION);
        hardware.rearRight.setMode  (DcMotor.RunMode.RUN_TO_POSITION);

        hardware.setLeftPower(speed);
        hardware.setRightPower(speed);

        double heading;
        double error;
        double correction;
        double newLeftSpeed;
        double newRightSpeed;
        double startTime = getRuntime();

        while(opModeIsActive() &&
                hardware.frontLeft.isBusy() &&
                hardware.frontRight.isBusy() &&
                hardware.rearLeft.isBusy() &&
                hardware.rearRight.isBusy() &&
                !gamepad1.dpad_down &&
                (getRuntime() - startTime) < timeout) {                // Canceled on dpad down

            heading = hardware.heading();
            error   = targetHeading - heading;
            correction = error * K_P;
            newLeftSpeed = Math.min((speed + correction), MAX_DRIVE_SPEED);
            newRightSpeed = Math.min((speed - correction), MAX_DRIVE_SPEED);

            // -ve versus +ve depends on whether cw or ccw rotation is +ve
            hardware.setLeftPower(newLeftSpeed);
            hardware.setRightPower(newRightSpeed);

            telemetry.addData("error", error);
            telemetry.addData("correction", correction);
            telemetry.addData("front left power",   hardware.frontLeft.getPower());
            telemetry.addData("front right power",  hardware.frontRight.getPower());
            telemetry.update();
        }

        hardware.setLeftPower(0.0);
        hardware.setRightPower(0.0);

        hardware.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }
    private void driveEncoderCountsGyroNoStop(int counts, double speed, double targetHeading, double timeout) {
        hardware.setDriveCounts(counts);

        hardware.frontLeft.setMode  (DcMotor.RunMode.RUN_TO_POSITION);
        hardware.frontRight.setMode (DcMotor.RunMode.RUN_TO_POSITION);
        hardware.rearLeft.setMode   (DcMotor.RunMode.RUN_TO_POSITION);
        hardware.rearRight.setMode  (DcMotor.RunMode.RUN_TO_POSITION);

        hardware.setLeftPower(speed);
        hardware.setRightPower(speed);

        double heading;
        double error;
        double correction;
        double newLeftSpeed;
        double newRightSpeed;
        double startTime = getRuntime();

        while(opModeIsActive() &&
                hardware.frontLeft.isBusy() &&
                hardware.frontRight.isBusy() &&
                hardware.rearLeft.isBusy() &&
                hardware.rearRight.isBusy() &&
                !gamepad1.dpad_down &&
                (getRuntime() - startTime) < timeout) {                // Canceled on dpad down

            heading = hardware.heading();
            error   = targetHeading - heading;
            correction = error * K_P;
            newLeftSpeed = Math.min((speed + correction), MAX_DRIVE_SPEED);
            newRightSpeed = Math.min((speed - correction), MAX_DRIVE_SPEED);

            // -ve versus +ve depends on whether cw or ccw rotation is +ve
            hardware.setLeftPower(newLeftSpeed);
            hardware.setRightPower(newRightSpeed);

            telemetry.addData("error", error);
            telemetry.addData("correction", correction);
            telemetry.addData("front left power",   hardware.frontLeft.getPower());
            telemetry.addData("front right power",  hardware.frontRight.getPower());
            telemetry.update();
        }

        hardware.frontLeft. setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.rearLeft.  setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.rearRight. setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }


}
