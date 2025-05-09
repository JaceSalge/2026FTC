// Importing package.
package org.firstinspires.ftc.teamcode;

//Importing libraries.
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.JavaUtil;

// Naming.
@TeleOp(name = "TeleOpB0B")
// Main class.
public class TeleOpB0B extends LinearOpMode {
  // Class variable definition.
  private Servo grabTilt;
  private DcMotor lb;
  private DcMotor lf;
  private DcMotor rb;
  private DcMotor rf;
  private DcMotor angularRotation;
  private DcMotor upDown;
  private CRServo gripper;
  
  // Override.
  @Override
  // Run on initialization.
  public void runOpMode() {
    // Variable declaration.
    int INTAKE_COLLECT;
    int INTAKE_OFF;
    double INTAKE_DEPOSIT;
    float axial;
    float lateral;
    float yaw;
    float rightFrontPower;
    float leftFrontPower;
    float leftBackPower;
    float rightBackPower;
    double max;

    // Initializing moving components.
    grabTilt = hardwareMap.get(Servo.class, "grabTilt");
    lb = hardwareMap.get(DcMotor.class, "lb");
    lf = hardwareMap.get(DcMotor.class, "lf");
    rb = hardwareMap.get(DcMotor.class, "rb");
    rf = hardwareMap.get(DcMotor.class, "rf");
    angularRotation = hardwareMap.get(DcMotor.class, "angularRotation");
    upDown = hardwareMap.get(DcMotor.class, "upDown");
    gripper = hardwareMap.get(CRServo.class, "gripper");

    // Initializing values.
    grabTilt.setPosition(0.05);
    lb.setDirection(DcMotor.Direction.REVERSE);
    lf.setDirection(DcMotor.Direction.REVERSE);
    rb.setDirection(DcMotor.Direction.REVERSE);
    rf.setDirection(DcMotor.Direction.FORWARD);
    INTAKE_COLLECT = -1;
    INTAKE_OFF = 0;
    INTAKE_DEPOSIT = 0.5;
    // Wait for the driver to hit start.
    waitForStart();
    if (opModeIsActive()) {
      // Main loop.
      while (opModeIsActive()) {
        // Defining more moving component variables.
        axial = -gamepad1.left_stick_y;
        lateral = -gamepad1.left_stick_x;
        yaw = gamepad1.right_stick_x;
        rightFrontPower = axial + lateral + yaw;
        leftFrontPower = (axial - lateral) - yaw;
        leftBackPower = (axial - lateral) + yaw;
        rightBackPower = (axial + lateral) - yaw;
        // Normalize the values so no wheel power exceeds 100%. This ensures that the robot maintains the desired motion.
        max = JavaUtil.maxOfList(JavaUtil.createListWith(Math.abs(leftFrontPower), Math.abs(rightFrontPower), Math.abs(leftBackPower), Math.abs(rightBackPower)));
        if (max > 1) {
          leftFrontPower = (float) (leftFrontPower / max);
          rightFrontPower = (float) (rightFrontPower / max);
          leftBackPower = (float) (leftBackPower / max);
          rightBackPower = (float) (rightBackPower / max);
        }
        // Send calculated power to moving components.
        lf.setPower(leftFrontPower);
        rf.setPower(rightFrontPower);
        lb.setPower(leftBackPower);
        rb.setPower(rightBackPower);
        if (gamepad1.dpad_up) {
          angularRotation.setPower(1);
        } else if (gamepad1.dpad_down) {
          angularRotation.setPower(-1);
        } else {
          angularRotation.setPower(0.009);
        }
        if (gamepad1.dpad_left) {
          upDown.setPower(0.5);
        } else if (gamepad1.dpad_right) {
          upDown.setPower(-0.5);
        } else {
          upDown.setPower(0);
        }
        if (gamepad1.a) {
          grabTilt.setPosition(0);
        } else if (gamepad1.b) {
          grabTilt.setPosition(0.4);
        }
        if (gamepad1.right_bumper) {
          gripper.setPower(INTAKE_COLLECT);
        } else {
          if (gamepad1.x) {
            gripper.setPower(INTAKE_OFF);
          } else {
            if (gamepad1.left_bumper) {
              gripper.setPower(INTAKE_DEPOSIT);
            }
          }
        }
        // Telemetry.
        telemetry.update();
        telemetry.addData("Motor Positions: 1", lf.getCurrentPosition());
        telemetry.addData("2", lb.getCurrentPosition());
        telemetry.addData("3", rb.getCurrentPosition());
        telemetry.addData("4", rf.getCurrentPosition());
        telemetry.addData("Grabber Tilt Value", grabTilt.getPosition());
      }
    }
  }
}
