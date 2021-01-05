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
    super.run();

    MapLocation enemySlanderer = senseRobot(RobotType.SLANDERER, enemyTeam);
    if (enemySlanderer != null) {
      if (tryExpose(enemySlanderer)) {
        return;
      }

      if (tryMoveTo(enemySlanderer)) {
        return;
      }
    }

    MapLocation enemyHq = senseRobot(RobotType.ENLIGHTENMENT_CENTER, enemyTeam);
    if (enemyHq != null) {
      if (tryMoveTo(enemyHq)) {
        return;
      }
    }

    tryMoveToEnemy();
  }

  private boolean tryExpose(MapLocation location) throws GameActionException {
    if (rc.canExpose(location)) {
      rc.expose(location);
      return true;
    }

    return false;
  }
}
