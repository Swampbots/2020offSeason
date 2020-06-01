package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

public class OffSeasonHardware {

    // Motors
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor rearLeft;
    public DcMotor rearRight;

    // Servos
    public CRServo intakeLeft;
    public CRServo intakeRight;

    // IMU
    BNO055IMU imu;


    public void init(HardwareMap hardwareMap, boolean initIMU) {

        // Get hardware from config file
        frontLeft   = hardwareMap.dcMotor.get("fl_drive");
        frontRight  = hardwareMap.dcMotor.get("fr_drive");
        rearLeft    = hardwareMap.dcMotor.get("rl_drive");
        rearRight   = hardwareMap.dcMotor.get("rr_drive");

        intakeLeft  = hardwareMap.crservo.get("intake_left");
        intakeRight = hardwareMap.crservo.get("intake_right");

        // Set hardware attributes
        frontLeft.  setDirection(DcMotorSimple.Direction.REVERSE);
        rearLeft.   setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.  setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.  setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearLeft.  setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.  setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        intakeLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        // IMU
        if(initIMU) {
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
            parameters.loggingEnabled      = true;
            parameters.loggingTag          = "IMU";
            parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

            imu = hardwareMap.get(BNO055IMU.class, "imu");
            imu.initialize(parameters);
        }
    }


    // Movement methods
    public void setMecanumPower(double drive, double strafe, double twist) {
        setMecanumPower(drive,strafe,twist,1);
    }

    public void setMecanumPower(double drive, double strafe, double twist, double speedMod) {
        frontLeft.setPower( (drive + strafe + twist) * speedMod);
        frontRight.setPower((drive - strafe - twist) * speedMod);
        rearLeft.setPower(  (drive - strafe + twist) * speedMod);
        rearRight.setPower( (drive + strafe - twist) * speedMod);
    }

    public void setLeftPower(double power) {
        frontLeft.setPower(power);
        rearLeft.setPower(power);
    }

    public void setRightPower(double power) {
        frontRight.setPower(power);
        rearRight.setPower(power);
    }


    // Attachment methods
    public void setIntakePower(double power) {
        intakeLeft  .setPower(power);
        intakeRight .setPower(power);
    }

    // Sensor methods
    public void setDriveCounts(int counts) {
        frontLeft.setTargetPosition    (frontLeft.getCurrentPosition()    + counts);
        frontRight.setTargetPosition   (frontRight.getCurrentPosition()   + counts);
        rearLeft.setTargetPosition     (rearLeft.getCurrentPosition()     + counts);
        rearRight.setTargetPosition    (rearRight.getCurrentPosition()    + counts);
    }

    public void setStrafeCounts(int counts) {
        frontLeft.setTargetPosition    (frontLeft.getCurrentPosition()    + counts);
        frontRight.setTargetPosition   (frontRight.getCurrentPosition()   - counts);
        rearLeft.setTargetPosition     (rearLeft.getCurrentPosition()     - counts);
        rearRight.setTargetPosition    (rearRight.getCurrentPosition()    + counts);
    }

    public float heading() {
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }

}
