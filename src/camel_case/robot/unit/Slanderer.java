package camel_case.robot.unit;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class Slanderer extends Unit {
  public Slanderer(RobotController rc) {
    super(rc, RobotType.SLANDERER, false);
  }

  @Override
  public void run() throws GameActionException {
    super.run();

    MapLocation enemyMuckraker = senseRobot(RobotType.MUCKRAKER, enemyTeam);
    if (enemyMuckraker != null) {
      if (tryMove(rc.getLocation().directionTo(enemyMuckraker).opposite())) {
        return;
      }
    }

    tryWanderSafe();
  }

  public Politician convertToPolitician() {
    return new Politician(rc, hq, mapInfo, boundaries);
  }
}
