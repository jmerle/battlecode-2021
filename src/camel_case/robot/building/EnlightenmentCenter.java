package camel_case.robot.building;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import camel_case.robot.Robot;

public class EnlightenmentCenter extends Robot {
  private final RobotType[] spawnOrder = {
    RobotType.MUCKRAKER,
    RobotType.SLANDERER,
    RobotType.SLANDERER,
    RobotType.SLANDERER,
    RobotType.SLANDERER
  };

  private int nextSpawnIndex = 0;

  public EnlightenmentCenter(RobotController rc) {
    super(rc, RobotType.ENLIGHTENMENT_CENTER);
  }

  @Override
  public void run() throws GameActionException {
    int spawnInfluence = (int) (50 + Math.floor((double) rc.getRoundNum() / 60));

    RobotType spawnType = spawnOrder[nextSpawnIndex];
    if (spawnType == RobotType.SLANDERER) {
      if (senseRobot(RobotType.MUCKRAKER, enemyTeam) != null) {
        spawnType = RobotType.POLITICIAN;
      }
    }

    if (trySpawn(spawnType, spawnInfluence)) {
      nextSpawnIndex = (nextSpawnIndex + 1) % spawnOrder.length;
      return;
    }

    double influence = rc.getInfluence();
    double bidPercentage = (double) rc.getRoundNum() / 150.0;
    tryBid((int) Math.round(influence / 100.0 * bidPercentage));
  }

  private boolean tryBid(int influence) throws GameActionException {
    if (rc.canBid(influence)) {
      rc.bid(influence);
      return true;
    }

    return false;
  }

  private boolean trySpawn(RobotType type, int influence) throws GameActionException {
    for (Direction direction : adjacentDirections) {
      if (tryBuildRobot(type, direction, influence)) {
        return true;
      }
    }

    return false;
  }

  private boolean tryBuildRobot(RobotType type, Direction direction, int influence)
      throws GameActionException {
    if (rc.canBuildRobot(type, direction, influence)) {
      rc.buildRobot(type, direction, influence);
      return true;
    }

    return false;
  }
}
