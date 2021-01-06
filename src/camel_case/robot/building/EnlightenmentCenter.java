package camel_case.robot.building;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import camel_case.robot.Robot;

public class EnlightenmentCenter extends Robot {
  private final RobotType[] spawnOrder = {
    RobotType.MUCKRAKER,
    RobotType.SLANDERER,
    RobotType.SLANDERER,
    RobotType.POLITICIAN,
    RobotType.POLITICIAN
  };

  private int nextSpawnIndex = 0;

  private final int[] spawnedRobots = new int[10000];
  private int spawnedRobotsCount = 0;

  public EnlightenmentCenter(RobotController rc) {
    super(rc, RobotType.ENLIGHTENMENT_CENTER, true);
  }

  @Override
  public void run() throws GameActionException {
    super.run();

    if (mapInfo == null) {
      parseFlagsFromSpawnedRobots();
      updateMapInfo();
    }

    int spawnInfluence = (int) (50 + Math.floor((double) rc.getRoundNum() / 60));

    RobotType spawnType = spawnOrder[nextSpawnIndex];
    if (spawnType == RobotType.SLANDERER && senseRobot(RobotType.MUCKRAKER, enemyTeam) != null) {
      spawnType = RobotType.POLITICIAN;
    }

    if (trySpawn(spawnType, spawnInfluence)) {
      nextSpawnIndex = (nextSpawnIndex + 1) % spawnOrder.length;
    }

    double influence = rc.getInfluence();
    double bidPercentage = (double) rc.getRoundNum() / 150.0;
    tryBid((int) Math.round(influence / 100.0 * bidPercentage));
  }

  private void parseFlagsFromSpawnedRobots() throws GameActionException {
    for (int i = 0; i < spawnedRobotsCount; i++) {
      int id = spawnedRobots[i];

      if (id == -1) {
        continue;
      }

      if (!rc.canGetFlag(id)) {
        spawnedRobots[i] = -1;
        continue;
      }

      parseFlag(rc.getFlag(id));
    }
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

      RobotInfo robot = rc.senseRobotAtLocation(rc.getLocation().add(direction));
      spawnedRobots[spawnedRobotsCount] = robot.getID();
      spawnedRobotsCount++;

      return true;
    }

    return false;
  }
}
