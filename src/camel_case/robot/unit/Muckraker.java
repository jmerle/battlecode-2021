package camel_case.robot.unit;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class Muckraker extends Unit {
  public Muckraker(RobotController rc) {
    super(rc, RobotType.MUCKRAKER);
  }

  @Override
  public void run() throws GameActionException {}
}
