package camel_case.robot.unit;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class Muckraker extends Unit {
  public Muckraker(RobotController rc) {
    super(rc, RobotType.MUCKRAKER);
  }

  @Override
  public void run() throws GameActionException {
    MapLocation enemySlanderer = senseRobot(RobotType.SLANDERER, enemyTeam);
    if (enemySlanderer != null) {
      if (tryExpose(enemySlanderer)) {
        return;
      }
    }

    tryMoveRandom();
  }

  private boolean tryExpose(MapLocation location) throws GameActionException {
    if (rc.canExpose(location)) {
      rc.expose(location);
      return true;
    }

    return false;
  }
}
