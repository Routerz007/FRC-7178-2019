/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.ElevatorMove;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.AnalogInput;

 public class Elevator extends Subsystem {
  public final static WPI_VictorSPX _elevatorLeft = RobotMap._elevatorLeft;
  public final static WPI_VictorSPX _elevatorRight = RobotMap._elevatorRight;
  public static AnalogInput _ai = new AnalogInput(0);
  //Hatch levels
  public static final double _lowHatch = 1;
  public static final double _midHatch = 2;
  public static final double _highHatch = 3;
  //Cargo levels
  public static final double _lowShoot = 1.0 ;
  public static final double _midShoot = 1.5;
  public static final double _highShoot = 4.09;
  //Bottom level
  public static final double _base = 0.5;
  //Threshold difference
  public static final double _threshold = 0.05;
  //Button Count
  public static double _hatchCount = 0;
  public static double _cargoCount = 0;
  //Target Height
  public static double _target = 0;
  //Elevator Speed
  public static final double _elevatorSpeed = 0.6;

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new ElevatorMove());
  }

  public void ElevatorSetUp() {
    _elevatorLeft.setInverted(true);
    _elevatorRight.setInverted(true);
    _elevatorRight.follow(_elevatorLeft);
    _elevatorLeft.setNeutralMode(NeutralMode.Brake);
    _elevatorRight.setNeutralMode(NeutralMode.Brake);
    
  }
  
  public void ElevatorShift() {

    if(Robot.m_oi.m_Controller2.getRawButton(1) == true) { //Hatch Up
      if(_ai.getAverageVoltage() < _lowHatch - _threshold) {
        _target = _lowHatch;
      } else if (_ai.getAverageVoltage() < _midHatch - _threshold) {
        _target= _midHatch;
      } else if (_ai.getAverageVoltage() < _highHatch - _threshold) {
        _target = _highHatch;
      }
    } else if (Robot.m_oi.m_Controller2.getRawButton(3) == true) { //Hatch Down
      if(_ai.getAverageVoltage() > _highHatch + _threshold) {
        _target = _highHatch;
      } else if (_ai.getAverageVoltage() > _midHatch + _threshold) {
        _target = _midHatch;
      } else if (_ai.getAverageVoltage() > _lowHatch + _threshold) {
        _target = _lowHatch;
      }
    } else if (Robot.m_oi.m_Controller2.getRawButton(2) == true) { //Cargo Up
      if(_ai.getAverageVoltage() < _lowShoot - _threshold) {
        _target = _lowShoot;
      } else if (_ai.getAverageVoltage() < _midShoot - _threshold) {
        _target = _midShoot;
      } else if (_ai.getAverageVoltage() < _highShoot - _threshold) {
        _target = _highShoot;
      }
    } else if (Robot.m_oi.m_Controller2.getRawButton(4) == true) { //Cargo Down
      if(_ai.getAverageVoltage() > _highShoot + _threshold) {
        _target = _highShoot;
      } else if (_ai.getAverageVoltage() > _midShoot + _threshold) {
        _target = _midShoot;
      } else if (_ai.getAverageVoltage() > _lowShoot + _threshold) {
        _target = _lowShoot;
      }
    } else if (Robot.m_oi.m_Controller2.getRawAxis(2) > 0.1) { //Overide Down
      _target = 0;
    } else if (Robot.m_oi.m_Controller2.getRawButton(5) == true) { //Overide Up
      _target = 0;
    } 

    if(_target != 0.0 ){
      if(_target < _base){
        _target = _base;
      }

      if(_target > _highShoot){
        _target = _highShoot;
      }

      if(_ai.getAverageVoltage() < _target - _threshold) {
        _elevatorLeft.set(_elevatorSpeed);
      } else if (_ai.getAverageVoltage() > _target + _threshold) {
        _elevatorLeft.set(-_elevatorSpeed);
      } else {
        _elevatorLeft.set(0);
      }  
    } else {
      if (Robot.m_oi.m_Controller2.getRawAxis(2) > 0.1) { //Overide Down
        if(_ai.getAverageVoltage() > 0.3){
          _elevatorLeft.set(-_elevatorSpeed);  
        } else {
          _elevatorLeft.set(0);  
        }
      } else if (Robot.m_oi.m_Controller2.getRawButton(5) == true) { //Overide Up
        if(_ai.getAverageVoltage() < _highShoot){
          _elevatorLeft.set(_elevatorSpeed);  
        } else {
          _elevatorLeft.set(0);
        }
      } else {
        _elevatorLeft.set(0);
      }
    }


    // if(Robot.m_oi.m_Controller2.getRawAxis(2) > 0.1) {
    //   _elevatorLeft.set(_elevatorSpeed);
    //   _elevatorRight.set(_elevatorSpeed);
    // } else if (Robot.m_oi.m_Controller2.getRawButton(5) == true) {
    //   _elevatorLeft.set(-_elevatorSpeed);
    //   _elevatorRight.set(-_elevatorSpeed);
    // } else {
    //   _elevatorLeft.set(0);
    //   _elevatorRight.set(0);
    // }

    

    // Button 1 make the Hatch Go UP
    // if(Robot.m_oi.m_Controller2.getRawButton(1) == true) {

    //   if(_ai.getAverageVoltage() > _lowHatch + _threshold && _hatchCount == 0) {
    //     _elevatorLeft.set(-_elevatorSpeed);
    //     _elevatorRight.set(-_elevatorSpeed);
    //     _hatchCount = _hatchCount + 1;
    //   } else if (_ai.getAverageVoltage() > _midHatch + _threshold && _hatchCount == 1) {
    //     _elevatorLeft.set(-_elevatorSpeed);
    //     _elevatorRight.set(-_elevatorSpeed);
    //     _hatchCount = _hatchCount + 1;
    //   } else if(_ai.getAverageVoltage() > _highHatch + _threshold && _hatchCount == 2) {
    //     _elevatorLeft.set(-_elevatorSpeed);
    //     _elevatorRight.set(-_elevatorSpeed);
    //     _hatchCount = _hatchCount + 1;
    //   } else {
    //     _elevatorLeft.set(0);
    //     _elevatorRight.set(0);
    //   }
    // }

    // if(Robot.m_oi.m_Controller2.getRawButton(3) == true) {
    //   if(_ai.getAverageVoltage() < _lowHatch - _threshold && _hatchCount == 1) {
    //     _elevatorLeft.set(_elevatorSpeed);
    //     _elevatorRight.set(_elevatorSpeed);
    //     _hatchCount = _hatchCount - 1;
    //   } else if(_ai.getAverageVoltage() < _midHatch - _threshold && _hatchCount == 2) {
    //     _elevatorLeft.set(_elevatorSpeed);
    //     _elevatorRight.set(_elevatorSpeed);
    //     _hatchCount = _hatchCount - 1;
    //   } else if(_ai.getAverageVoltage() < _highHatch - _threshold && _hatchCount == 3) {
    //     _elevatorLeft.set(-_elevatorSpeed);
    //     _elevatorRight.set(-_elevatorSpeed);
    //     _hatchCount = _hatchCount - 1;
    //   } else {
    //     _elevatorLeft.set(0);
    //     _elevatorRight.set(0);
    //   }
    // }

    // if(Robot.m_oi.m_Controller2.getRawButton(2) == true) {
    //     if(_ai.getAverageVoltage() > _lowShoot + _threshold && _cargoCount == 0) {
    //       _elevatorLeft.set(-_elevatorSpeed);
    //       _elevatorRight.set(-_elevatorSpeed);
    //       _cargoCount = _cargoCount + 1;
    //     } else if(_ai.getAverageVoltage() > _midShoot + _threshold && _cargoCount == 1) {
    //       _elevatorLeft.set(_elevatorSpeed);
    //       _elevatorRight.set(_elevatorSpeed);
    //       _cargoCount = _cargoCount + 1;
    //     } else if(_ai.getAverageVoltage() > _highShoot + _threshold && _cargoCount == 2) {
    //       _elevatorLeft.set(_elevatorSpeed);
    //       _elevatorRight.set(_elevatorSpeed);
    //       _cargoCount = _cargoCount + 1;
    //     } else {
    //       _elevatorLeft.set(0);
    //       _elevatorRight.set(0);
    //     }
    // }

    // if(Robot.m_oi.m_Controller2.getRawButton(4) == true) {
    //   if(_ai.getAverageVoltage() < _lowShoot - _threshold && _cargoCount == 1) {
    //     _elevatorLeft.set(_elevatorSpeed);
    //     _elevatorRight.set(_elevatorSpeed);
    //     _cargoCount = _cargoCount - 1;
    //   } else if(_ai.getAverageVoltage() < _midShoot - _threshold && _cargoCount == 2) {
    //     _elevatorLeft.set(_elevatorSpeed);
    //     _elevatorRight.set(_elevatorSpeed);
    //     _cargoCount = _cargoCount - 1;
    //   } else if(_ai.getAverageVoltage() < _highShoot - _threshold && _cargoCount == 3) {
    //     _elevatorLeft.set(_elevatorSpeed);
    //     _elevatorRight.set(_elevatorSpeed);
    //     _cargoCount = _cargoCount - 1;
    //   } else {
    //     _elevatorLeft.set(0);
    //     _elevatorRight.set(0);
    //   }
    // }

    // if(Robot.m_oi.m_Controller2.getRawButton(10) == true) {
    //   if(_ai.getAverageValue() > _base + _threshold) {
    //     _elevatorLeft.set(-_elevatorSpeed);
    //     _elevatorRight.set(-_elevatorSpeed);
    //   } else if(_ai.getAverageVoltage() < _base - _threshold) {
    //     _elevatorLeft.set(_elevatorSpeed);
    //     _elevatorRight.set(_elevatorSpeed);
    //   } else {
    //     _elevatorLeft.set(0);
    //     _elevatorRight.set(0);
    //   }
    // }
  SmartDashboard.putNumber("Accumulator Value", _ai.getAverageVoltage());
  SmartDashboard.putNumber("Target", _target);
  }
}
