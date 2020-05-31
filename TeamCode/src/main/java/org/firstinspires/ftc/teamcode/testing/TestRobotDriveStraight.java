package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.OffSeasonHardware;

@TeleOp(name = "Test: Drive Straight", group = "Testing")
public class TestRobotDriveStraight extends LinearOpMode {

    // Hardware
    OffSeasonHardware hardware = new OffSeasonHardware();
    private final boolean INIT_IMU = true;

    // Method-Specific
    private final double DRIVE_SPEED = 0.4; // Default drive speed
    private final double MAX_DRIVE_SPEED = Math.min(DRIVE_SPEED + 0.2, 1.0);
    private final double COUNTS_PER_INCH_EMPIRICAL = 1000 / 24.0;    // Determined by testing (1000 counts / 24.0 inches)
    private final double K_P = 0.1;   // Proportional coefficient for gyro-controlled driving

    @Override
    public void runOpMode() {
        telemetry.addLine("Initializing hardware");
        telemetry.update();

        hardware.init(hardwareMap, INIT_IMU);

        telemetry.addLine("Ready");
        telemetry.update();
        waitForStart();

        while(opModeIsActive()) {
            // Mecanum drive
            double drive = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double twist = -gamepad1.right_stick_x;
            hardware.setMecanumPower(drive, strafe, twist);

            // Intake controls
            /*
            if(gamepad2.b) {
                hardware.setIntakePower(1.0);
            } else if (gamepad2.y) {
                hardware.setIntakePower(-1.0);
            } else {
                hardware.setIntakePower(0.0);
            }
            */

            // Drive straight controls
            if(gamepad1.x) driveInchesGyro(48.0, DRIVE_SPEED, hardware.heading());


            telemetry.addLine("Running");
            telemetry.addData("Heading", hardware.heading());
            telemetry.update();
        }
    }


    // Encoder-controlled movement
    private void driveInches(double inches) {
        driveInches(inches, DRIVE_SPEED);   // Defaults to local field member speed
    }
    private void driveInches(double inches, double speed) {
        driveEncoderCounts((int)(inches * COUNTS_PER_INCH_EMPIRICAL), speed);
    }
    private void driveInchesGyro(double inches, double speed, double targetHeading) {
        driveEncoderCountsGyro((int)(inches * COUNTS_PER_INCH_EMPIRICAL), speed, targetHeading);
    }

    private void driveEncoderCounts(int counts, double speed) {
        hardware.setDriveCounts(counts);

        hardware.frontLeft.setMode  (DcMotor.RunMode.RUN_TO_POSITION);
        hardware.frontRight.setMode (DcMotor.RunMode.RUN_TO_POSITION);
        hardware.rearLeft.setMode   (DcMotor.RunMode.RUN_TO_POSITION);
        hardware.rearRight.setMode  (DcMotor.RunMode.RUN_TO_POSITION);

        hardware.setLeftPower(speed);
        hardware.setRightPower(speed);

        while(opModeIsActive() &&
                hardware.frontLeft.isBusy() &&
                hardware.frontRight.isBusy() &&
                hardware.rearLeft.isBusy() &&
                hardware.rearRight.isBusy()) {
            telemetry.addData("Front left encoder",     hardware.frontLeft.getCurrentPosition());
            telemetry.addData("Front right encoder",    hardware.frontRight.getCurrentPosition());
            telemetry.addData("Rear left encoder",      hardware.rearLeft.getCurrentPosition());
            telemetry.addData("Rear right encoder",     hardware.rearRight.getCurrentPosition());
            telemetry.update();
        }

        hardware.setLeftPower(0.0);
        hardware.setRightPower(0.0);

        hardware.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }
    private void driveEncoderCountsGyro(int counts, double speed, double targetHeading) {
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

        while(opModeIsActive() &&
                hardware.frontLeft.isBusy() &&
                hardware.frontRight.isBusy() &&
                hardware.rearLeft.isBusy() &&
                hardware.rearRight.isBusy()) {

            heading = hardware.heading();
            error   = targetHeading - heading;
            correction = error * K_P;

            // -ve versus +ve depends on whether cw or ccw rotation is +ve
            hardware.setLeftPower(speed - correction);
            hardware.setRightPower(speed + correction);

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

}
