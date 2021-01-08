package camel_case.robot.unit;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import camel_case.robot.Robot;
import camel_case.util.ArrayUtils;

public abstract class Unit extends Robot {
  protected MapLocation hq = null;

  private final Direction[] wanderDirections = ArrayUtils.shuffle(adjacentDirections.clone());
  private MapLocation[] wanderTargets = null;
  private int currentWanderIndex = 0;

  public Unit(RobotController rc, RobotType type, boolean checkForMapBoundaries) {
    super(rc, type, checkForMapBoundaries);
  }

  @Override
  public void run() throws GameActionException {
    super.run();

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
    for (Direction direction : ArrayUtils.shuffle(adjacentDirections.clone())) {
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

  protected boolean tryHide() throws GameActionException {
    if (hq == null || mapInfo == null) {
      return tryWanderSafe();
    }

    int x = hq.x <= mapInfo.minX + mapInfo.size / 2 ? mapInfo.minX : mapInfo.maxX;
    int y = hq.y <= mapInfo.minY + mapInfo.size / 2 ? mapInfo.minY : mapInfo.maxY;
    MapLocation target = new MapLocation(x, y);

    if (tryMoveTo(target)) {
      return true;
    }

    return tryWanderSafe();
  }

  protected boolean tryWander() throws GameActionException {
    if (hq != null && mapInfo != null && wanderTargets == null) {
      wanderTargets = getWanderTargets();
      currentWanderIndex = 0;
    }

    if (wanderTargets == null) {
      if (tryWanderDirections()) {
        return true;
      }
    } else {
      if (tryWanderTargets()) {
        return true;
      }
    }

    return tryMoveRandom();
  }

  private boolean tryWanderDirections() throws GameActionException {
    MapLocation location = rc.getLocation();
    while (true) {
      location = location.add(wanderDirections[currentWanderIndex]);

      if (getDistanceTo(location) > me.sensorRadiusSquared) {
        break;
      }

      if (!rc.onTheMap(location)) {
        if (mapInfo == null) {
          while (true) {
            currentWanderIndex = (currentWanderIndex + 1) % wanderDirections.length;
            Direction direction = wanderDirections[currentWanderIndex];

            if ((boundaries[0] == -1 || boundaries[2] == -1)
                && (direction == Direction.NORTHWEST
                    || direction == Direction.WEST
                    || direction == Direction.SOUTHWEST
                    || direction == Direction.SOUTH
                    || direction == Direction.SOUTHEAST)) {
              break;
            }

            if (boundaries[1] == -1
                && (direction == Direction.NORTHEAST
                    || direction == Direction.EAST
                    || direction == Direction.SOUTHEAST)) {
              break;
            }

            if (boundaries[3] == -1
                && (direction == Direction.SOUTHWEST
                    || direction == Direction.SOUTH
                    || direction == Direction.SOUTHEAST)) {
              break;
            }
          }
        } else {
          currentWanderIndex = (currentWanderIndex + 1) % wanderDirections.length;
        }

        break;
      }
    }

    return tryMove(wanderDirections[currentWanderIndex]);
  }

  private MapLocation[] getWanderTargets() {
    int reflectedX = mapInfo.xOffset + (mapInfo.size - 1 - (hq.x - mapInfo.xOffset));
    int reflectedY = mapInfo.yOffset + (mapInfo.size - 1 - (hq.y - mapInfo.yOffset));

    MapLocation[] otherLocations =
        new MapLocation[] {
          new MapLocation(reflectedX, hq.y),
          new MapLocation(hq.x, reflectedY),
          new MapLocation(reflectedX, reflectedY)
        };

    ArrayUtils.shuffle(otherLocations);

    return new MapLocation[] {
      otherLocations[0], otherLocations[1], otherLocations[2], new MapLocation(hq.x, hq.y)
    };
  }

  private boolean tryWanderTargets() throws GameActionException {
    if (rc.canSenseLocation(wanderTargets[currentWanderIndex])) {
      currentWanderIndex = (currentWanderIndex + 1) % wanderTargets.length;
    }

    return tryMoveTo(wanderTargets[currentWanderIndex]);
  }
}
