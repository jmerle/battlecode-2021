package camel_case_v7_sprint_2.robot.unit;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class Muckraker extends Unit {
  public Muckraker(RobotController rc) {
    super(rc, RobotType.MUCKRAKER, true);
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

    if (rc.getID() % 2 == 0) {
      if (tryWander()) {
        return;
      }
    } else {
      if (tryWanderDirections()) {
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
