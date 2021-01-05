package camel_case.robot.unit;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import camel_case.robot.Robot;
import camel_case.util.BetterRandom;

public abstract class Unit extends Robot {
  public Unit(RobotController rc, RobotType type) {
    super(rc, type);
  }

  protected boolean tryMove(Direction direction) throws GameActionException {
    if (rc.canMove(direction)) {
      rc.move(direction);
      return true;
    }

    return false;
  }

  protected boolean tryMoveRandom() throws GameActionException {
    return tryMove(adjacentDirections[BetterRandom.nextInt(adjacentDirections.length)]);
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
}
