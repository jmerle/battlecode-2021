package camel_case_v4.robot.building;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import camel_case_v4.robot.Robot;
import camel_case_v4.util.FlagType;
import camel_case_v4.util.Painter;

public class EnlightenmentCenter extends Robot {
  private final RobotType[] spawnOrder = {
    RobotType.MUCKRAKER,
    RobotType.SLANDERER,
    RobotType.MUCKRAKER,
    RobotType.SLANDERER,
    RobotType.MUCKRAKER,
    RobotType.SLANDERER,
    RobotType.MUCKRAKER,
    RobotType.POLITICIAN,
    RobotType.MUCKRAKER,
    RobotType.POLITICIAN,
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

    RobotType spawnType = spawnOrder[nextSpawnIndex];
    if (senseRobot(RobotType.MUCKRAKER, enemyTeam) != null
        || senseRobot(RobotType.POLITICIAN, enemyTeam) != null) {
      spawnType = RobotType.POLITICIAN;
    }

    int spawnInfluence = 0;
    switch (spawnType) {
      case MUCKRAKER:
        spawnInfluence = 1;
        break;
      case POLITICIAN:
        spawnInfluence = Math.max(11, rc.getInfluence() / 10);
        break;
      case SLANDERER:
        spawnInfluence = Math.max(Math.min(rc.getInfluence(), 1000), 100);
        break;
    }

    if (trySpawn(spawnType, spawnInfluence)) {
      nextSpawnIndex = (nextSpawnIndex + 1) % spawnOrder.length;
    }

    double influence = rc.getInfluence();
    double bidPercentage = (double) rc.getRoundNum() / 150.0;
    tryBid((int) Math.round(influence / 100.0 * bidPercentage));
  }

  @Override
  protected void onFlag(FlagType type, int value) {
    super.onFlag(type, value);

    if (type == FlagType.TARGET_EC) {
      setFlag(type, value);
    }
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
