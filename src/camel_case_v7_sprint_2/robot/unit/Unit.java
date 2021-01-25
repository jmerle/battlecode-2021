package camel_case_v7_sprint_2.robot.unit;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import camel_case_v7_sprint_2.robot.Robot;
import camel_case_v7_sprint_2.util.ArrayUtils;
import camel_case_v7_sprint_2.util.FlagType;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class Unit extends Robot {
  protected MapLocation hq = null;

  private final Set<Integer> hqIds = new HashSet<>();

  private final Direction[] wanderDirections = ArrayUtils.shuffle(adjacentDirections.clone());
  private int currentDirectionsIndex = 0;

  private final Set<MapLocation> knownTargets = new HashSet<>();
  private final Set<MapLocation> possibleTargets = new HashSet<>();
  protected final Set<MapLocation> ignoredTargets = new HashSet<>();

  private boolean addedReflectedTargets = false;

  public Unit(RobotController rc, RobotType type, boolean checkForMapBoundaries) {
    super(rc, type, checkForMapBoundaries);
  }

  @Override
  public void run() throws GameActionException {
    super.run();

    if (hq == null) {
      hq = senseRobot(RobotType.ENLIGHTENMENT_CENTER, myTeam);
    }

    for (RobotInfo robot : rc.senseNearbyRobots()) {
      if (robot.type == RobotType.ENLIGHTENMENT_CENTER && robot.team == myTeam) {
        hqIds.add(robot.ID);
      }

      if (robot.type == RobotType.ENLIGHTENMENT_CENTER && robot.team == enemyTeam) {
        MapLocation location = robot.location;

        ignoredTargets.remove(location);
        possibleTargets.remove(location);
        knownTargets.add(location);
      }
    }

    for (int id : hqIds) {
      if (rc.canGetFlag(id)) {
        parseFlag(id);
      }
    }

    cleanTargetCollection(knownTargets, possibleTargets);
    cleanTargetCollection(possibleTargets, knownTargets);

    if (mapInfo != null) {
      MapLocation closestKnownTarget = getClosestLocation(knownTargets);
      int newFlag = closestKnownTarget != null ? mapInfo.encodeLocation(closestKnownTarget) : -1;
      setFlag(FlagType.TARGET_EC, newFlag);
    }

    if (hq != null && mapInfo != null && !addedReflectedTargets) {
      int reflectedX = mapInfo.xOffset + (mapInfo.width - 1 - (hq.x - mapInfo.xOffset));
      int reflectedY = mapInfo.yOffset + (mapInfo.height - 1 - (hq.y - mapInfo.yOffset));

      int middleX = mapInfo.minX + mapInfo.width / 2;
      int middleY = mapInfo.minY + mapInfo.height / 2;

      possibleTargets.add(new MapLocation(reflectedX, hq.y));
      possibleTargets.add(new MapLocation(hq.x, reflectedY));
      possibleTargets.add(new MapLocation(reflectedX, reflectedY));
      possibleTargets.add(new MapLocation(middleX, middleY));

      addedReflectedTargets = true;
    }
  }

  private void cleanTargetCollection(Set<MapLocation> targets, Set<MapLocation> otherTargets)
      throws GameActionException {
    Iterator<MapLocation> it = targets.iterator();
    while (it.hasNext()) {
      MapLocation target = it.next();

      if (!rc.canSenseLocation(target)) {
        continue;
      }

      RobotInfo robot = rc.senseRobotAtLocation(target);

      if (robot == null || robot.team != enemyTeam) {
        it.remove();
        otherTargets.remove(target);
        ignoredTargets.add(target);
      }
    }
  }

  @Override
  protected void onFlag(FlagType type, int value) {
    super.onFlag(type, value);

    if (type == FlagType.TARGET_EC && mapInfo != null) {
      MapLocation target = mapInfo.decodeLocation(value);

      if (!ignoredTargets.contains(target)) {
        possibleTargets.remove(target);
        knownTargets.add(target);
      }
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
    MapLocation myLocation = rc.getLocation();

    Direction direction = myLocation.directionTo(location);
    Direction[] possibleDirections = {direction, direction.rotateLeft(), direction.rotateRight()};

    Direction bestDirection = null;
    double bestPassability = 0.0;

    for (Direction possibleDirection : possibleDirections) {
      MapLocation newLocation = myLocation.add(possibleDirection);

      if (!rc.canMove(possibleDirection)) {
        continue;
      }

      double passability = rc.sensePassability(newLocation);

      if (bestDirection == null || passability > bestPassability) {
        bestDirection = possibleDirection;
        bestPassability = passability;
      }
    }

    if (bestDirection == null) {
      return false;
    }

    return tryMove(bestDirection);
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

    int x = hq.x <= mapInfo.minX + mapInfo.width / 2 ? mapInfo.minX : mapInfo.maxX;
    int y = hq.y <= mapInfo.minY + mapInfo.height / 2 ? mapInfo.minY : mapInfo.maxY;
    MapLocation target = new MapLocation(x, y);

    if (tryMoveTo(target)) {
      return true;
    }

    return tryWanderSafe();
  }

  protected boolean tryWander() throws GameActionException {
    if (knownTargets.size() > 0) {
      if (tryMoveTo(getClosestLocation(knownTargets))) {
        return true;
      }
    } else if (possibleTargets.size() > 0) {
      if (tryMoveTo(getClosestLocation(possibleTargets))) {
        return true;
      }
    } else {
      if (tryWanderDirections()) {
        return true;
      }
    }

    return tryMoveRandom();
  }

  protected boolean tryWanderDirections() throws GameActionException {
    MapLocation location = rc.getLocation();
    while (true) {
      location = location.add(wanderDirections[currentDirectionsIndex]);

      if (getDistanceTo(location) > me.sensorRadiusSquared) {
        break;
      }

      if (!rc.onTheMap(location)) {
        if (mapInfo == null) {
          while (true) {
            currentDirectionsIndex = (currentDirectionsIndex + 1) % wanderDirections.length;
            Direction direction = wanderDirections[currentDirectionsIndex];

            if (boundaries[0] == -1 && direction == Direction.WEST) {
              break;
            }

            if (boundaries[1] == -1 && direction == Direction.EAST) {
              break;
            }

            if (boundaries[2] == -1 && direction == Direction.SOUTH) {
              break;
            }

            if (boundaries[3] == -1 && direction == Direction.NORTH) {
              break;
            }
          }
        } else {
          currentDirectionsIndex = (currentDirectionsIndex + 1) % wanderDirections.length;
        }

        break;
      }
    }

    return tryMove(wanderDirections[currentDirectionsIndex]);
  }

  private MapLocation getClosestLocation(Iterable<MapLocation> locations) {
    MapLocation closestLocation = null;
    int closestDistance = 0;

    for (MapLocation location : locations) {
      int distance = getDistanceTo(location);

      if (closestLocation == null || distance < closestDistance) {
        closestLocation = location;
        closestDistance = distance;
      }
    }

    return closestLocation;
  }
}
