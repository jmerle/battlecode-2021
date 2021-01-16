package camel_case.robot.building;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import camel_case.robot.Robot;
import camel_case.util.ArrayUtils;
import camel_case.util.FlagType;
import camel_case.util.Painter;

public class EnlightenmentCenter extends Robot {
  private final RobotType[] spawnOrder = {
    RobotType.MUCKRAKER,
    RobotType.SLANDERER,
    RobotType.POLITICIAN,
    RobotType.MUCKRAKER,
    RobotType.SLANDERER,
    RobotType.POLITICIAN,
    RobotType.MUCKRAKER,
    RobotType.SLANDERER,
    RobotType.POLITICIAN,
    RobotType.MUCKRAKER,
    RobotType.POLITICIAN,
    RobotType.MUCKRAKER,
    RobotType.POLITICIAN,
  };

  private int nextSpawnIndex = 0;

  private final int[] spawnedRobots = new int[GameConstants.GAME_MAX_NUMBER_OF_ROUNDS];
  private int spawnedRobotsCount = 0;

  private Direction[] offensiveSpawnDirections = ArrayUtils.shuffle(adjacentDirections.clone());
  private Direction[] defensiveSpawnDirections = offensiveSpawnDirections;
  private boolean spawnDirectionsRandom = true;

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

      if (spawnDirectionsRandom) {
        optimizeSpawnDirections();
        spawnDirectionsRandom = false;
      }
    }

    RobotType spawnType = spawnOrder[nextSpawnIndex];
    if (senseRobot(RobotType.POLITICIAN, enemyTeam) != null
        || senseRobot(RobotType.ENLIGHTENMENT_CENTER, enemyTeam) != null
        || (spawnType == RobotType.SLANDERER
            && senseRobot(RobotType.MUCKRAKER, enemyTeam) != null)) {
      spawnType = RobotType.POLITICIAN;
    }

    int spawnInfluence = 0;
    switch (spawnType) {
      case MUCKRAKER:
        spawnInfluence = 1;
        break;
      case POLITICIAN:
        spawnInfluence = Math.max(Math.min(rc.getInfluence() / 10, 10000), 20);
        break;
      case SLANDERER:
        spawnInfluence = Math.max(Math.min(rc.getInfluence(), 1000), 100);
        break;
    }

    if (trySpawn(spawnType, spawnInfluence)) {
      nextSpawnIndex = (nextSpawnIndex + 1) % spawnOrder.length;
    }

    if (rc.getRoundNum() > 250
        && rc.getTeamVotes() < GameConstants.GAME_MAX_NUMBER_OF_ROUNDS / 2 + 1) {
      double influence = rc.getInfluence();
      double bidPercentage =
          (double) rc.getRoundNum() / ((double) GameConstants.GAME_MAX_NUMBER_OF_ROUNDS / 10.0);
      int maxBidAmount = (int) Math.round(influence / 100.0 * bidPercentage);

      tryBid(Math.min(50000, maxBidAmount));
    }
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

      parseFlag(id);
    }
  }

  private void drawMessage() {
    if (painter == null) {
      painter = new Painter(rc, mapInfo);
    }

    painter.paintText(new String[] {"Eindhoven", "de", "Gekste", "", "camel_case"});
  }

  private void optimizeSpawnDirections() {
    offensiveSpawnDirections = new Direction[8];

    MapLocation location = rc.getLocation();
    String dx = location.x <= mapInfo.minX + mapInfo.width / 2 ? "EAST" : "WEST";
    String dy = location.y <= mapInfo.minY + mapInfo.height / 2 ? "NORTH" : "SOUTH";
    offensiveSpawnDirections[0] = Direction.valueOf(dy + dx);

    offensiveSpawnDirections[1] = offensiveSpawnDirections[0].rotateLeft();
    offensiveSpawnDirections[2] = offensiveSpawnDirections[0].rotateRight();

    offensiveSpawnDirections[3] = offensiveSpawnDirections[1].rotateLeft();
    offensiveSpawnDirections[4] = offensiveSpawnDirections[2].rotateRight();

    offensiveSpawnDirections[5] = offensiveSpawnDirections[3].rotateLeft();
    offensiveSpawnDirections[6] = offensiveSpawnDirections[4].rotateRight();

    offensiveSpawnDirections[7] = offensiveSpawnDirections[0].opposite();

    defensiveSpawnDirections = ArrayUtils.reverse(offensiveSpawnDirections.clone());
  }

  private boolean tryBid(int influence) throws GameActionException {
    if (rc.canBid(influence)) {
      rc.bid(influence);
      return true;
    }

    return false;
  }

  private boolean trySpawn(RobotType type, int influence) throws GameActionException {
    Direction[] spawnDirections =
        type == RobotType.SLANDERER ? defensiveSpawnDirections : offensiveSpawnDirections;

    for (Direction direction : spawnDirections) {
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
