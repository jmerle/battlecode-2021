package camel_case.robot.unit;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import camel_case.robot.Robot;
import camel_case.util.BetterRandom;

public abstract class Unit extends Robot {
  protected MapLocation hq = null;

  protected Direction[] enemyDirections = {
    Direction.WEST,
    Direction.SOUTH,
    Direction.NORTH,
    Direction.EAST,
    Direction.SOUTH,
    Direction.WEST,
    Direction.NORTHEAST,
    Direction.WEST,
    Direction.SOUTHEAST,
    Direction.WEST,
  };

  protected int currentEnemyDirection = 0;

  public Unit(RobotController rc, RobotType type) {
    super(rc, type);
  }

  @Override
  public void run() throws GameActionException {
    if (hq == null) {
      hq = senseRobot(RobotType.ENLIGHTENMENT_CENTER, myTeam);
    }
  }

  protected boolean tryMove(Direction direction) throws GameActionException {
    if (rc.canMove(direction)) {
      rc.move(direction);
      return true;
    }

    return false;
  }

  protected boolean tryMoveRandom() throws GameActionException {
    Direction[] directions = adjacentDirections.clone();

    for (int i = directions.length; i > 1; i--) {
      int a = i - 1;
      int b = BetterRandom.nextInt(i);

      Direction temp = directions[a];
      directions[a] = directions[b];
      directions[b] = temp;
    }

    for (Direction direction : directions) {
      if (tryMove(direction)) {
        return true;
      }
    }

    return false;
  }

  protected boolean tryMoveTo(MapLocation location) throws GameActionException {
    Direction direction = rc.getLocation().directionTo(location);

    if (tryMove(direction)) {
      return true;
    }

    for (int i = 1; i < 2; i++) {
      Direction rotatedLeft = direction;
      for (int j = 0; j < i; j++) {
        rotatedLeft = rotatedLeft.rotateLeft();
      }

      if (tryMove(rotatedLeft)) {
        return true;
      }

      Direction rotatedRight = direction;
      for (int j = 0; j < i; j++) {
        rotatedRight = rotatedRight.rotateRight();
      }

      if (tryMove(rotatedRight)) {
        return true;
      }
    }

    return false;
  }

  protected boolean tryWanderSafe() throws GameActionException {
    if (hq == null) {
      return tryMoveRandom();
    }

    if (rc.canSenseLocation(hq)) {
      return tryMoveRandom();
    }

    return tryMoveTo(hq);
  }

  protected boolean tryMoveToEnemy() throws GameActionException {
    MapLocation currentLocation = rc.getLocation();
    while (true) {
      currentLocation = currentLocation.add(enemyDirections[currentEnemyDirection]);

      if (getDistanceTo(currentLocation) > me.sensorRadiusSquared) {
        break;
      }

      if (!rc.onTheMap(currentLocation)) {
        currentEnemyDirection = (currentEnemyDirection + 1) % enemyDirections.length;
        break;
      }
    }

    if (tryMove(enemyDirections[currentEnemyDirection])) {
      return true;
    }

    return tryMoveRandom();
  }
}
