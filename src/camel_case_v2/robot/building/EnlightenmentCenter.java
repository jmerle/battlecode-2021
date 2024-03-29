package camel_case_v2.robot.building;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import camel_case_v2.robot.Robot;
import camel_case_v2.util.Painter;

public class EnlightenmentCenter extends Robot {
  private final RobotType[] spawnOrder = {
    RobotType.SLANDERER,
    RobotType.SLANDERER,
    RobotType.MUCKRAKER,
    RobotType.SLANDERER,
    RobotType.POLITICIAN,
    RobotType.POLITICIAN
  };

  private int nextSpawnIndex = 0;

  private final int[] spawnedRobots = new int[GameConstants.GAME_MAX_NUMBER_OF_ROUNDS];
  private int spawnedRobotsCount = 0;

  private Painter painter = null;

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

    if (mapInfo != null) {
      drawMessage();
    }

    int spawnInfluence = (int) (100 + Math.floor((double) rc.getRoundNum() / 60));

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

  private void drawMessage() {
    if (painter == null) {
      painter = new Painter(rc, mapInfo);
    }

    painter.paintText(new String[] {"Eindhoven", "de", "Gekste", "", "camel_case"});
  }
}
