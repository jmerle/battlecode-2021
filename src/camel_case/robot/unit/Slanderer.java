package camel_case.robot.unit;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class Slanderer extends Unit {
  public Slanderer(RobotController rc) {
    super(rc, RobotType.SLANDERER);
  }

  @Override
  public void run() throws GameActionException {}
}
