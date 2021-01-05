package camel_case.robot.unit;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import camel_case.robot.Robot;
import camel_case.util.BetterRandom;

public abstract class Unit extends Robot {
  private MapLocation hq = null;

  private Direction[] wanderDirections = getShuffledAdjacentDirections();
  private int currentWanderDirection = 0;

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
    for (Direction direction : getShuffledAdjacentDirections()) {
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

  protected boolean tryWander() throws GameActionException {
    MapLocation location = rc.getLocation();
    while (true) {
      location = location.add(wanderDirections[currentWanderDirection]);

      if (getDistanceTo(location) > me.sensorRadiusSquared) {
        break;
      }

      if (!rc.onTheMap(location)) {
        currentWanderDirection = (currentWanderDirection + 1) % wanderDirections.length;
        break;
      }
    }

    if (tryMove(wanderDirections[currentWanderDirection])) {
      return true;
    }

    return tryMoveRandom();
  }

  private Direction[] getShuffledAdjacentDirections() {
    Direction[] directions = adjacentDirections.clone();

    for (int i = directions.length - 1; i > 0; i--) {
      int index = BetterRandom.nextInt(i + 1);

      Direction temp = directions[index];
      directions[index] = directions[i];
      directions[i] = temp;
    }

    return directions;
  }
}
