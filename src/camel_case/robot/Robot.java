package camel_case.robot;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;
import camel_case.util.Color;
import camel_case.util.MapInfo;

public abstract class Robot {
  protected final RobotController rc;

  protected final RobotType me;
  private final boolean checkForMapBoundaries;

  protected Team myTeam;
  protected Team enemyTeam;

  protected MapInfo mapInfo = null;

  private final int[] boundaries = {-1, -1, -1, -1};
  private int currentFlagIndex = 0;

  protected Direction[] adjacentDirections = {
    Direction.NORTH,
    Direction.EAST,
    Direction.SOUTH,
    Direction.WEST,
    Direction.NORTHEAST,
    Direction.SOUTHEAST,
    Direction.SOUTHWEST,
    Direction.NORTHWEST
  };

  public Robot(RobotController rc, RobotType type, boolean checkForMapBoundaries) {
    this.rc = rc;
    this.checkForMapBoundaries = checkForMapBoundaries;

    me = type;

    myTeam = rc.getTeam();
    enemyTeam = myTeam.opponent();
  }

  public void run() throws GameActionException {
    if (mapInfo == null) {
      for (RobotInfo robot : rc.senseNearbyRobots(me.detectionRadiusSquared, myTeam)) {
        parseFlag(rc.getFlag(robot.getID()));
      }

      updateMapInfo();
    }

    if (mapInfo == null && checkForMapBoundaries) {
      detectMapBoundaries();
    }
  }

  public void updateFlag() throws GameActionException {
    int currentFlag = rc.getFlag(rc.getID());

    for (int i = 0; i < 4; i++) {
      int index = (currentFlagIndex + i) % 4;

      if (boundaries[index] == -1) {
        continue;
      }

      int newFlag = Integer.parseInt("1" + index + boundaries[index]);

      if (currentFlag == 0 || newFlag != currentFlag) {
        rc.setFlag(newFlag);
        currentFlagIndex = index;
        break;
      }
    }
  }

  protected void parseFlag(int flag) {
    if (flag == 0) {
      return;
    }

    String flagStr = Integer.toString(flag);

    if (flagStr.length() != 7) {
      return;
    }

    int index = Integer.parseInt(flagStr.substring(1, 2));
    int coordinate = Integer.parseInt(flagStr.substring(2));

    boundaries[index] = coordinate;
  }

  protected void updateMapInfo() {
    if (mapInfo != null) {
      return;
    }

    mapInfo = MapInfo.parseBoundaries(boundaries[0], boundaries[1], boundaries[2], boundaries[3]);
  }

  private void detectMapBoundaries() throws GameActionException {
    if (boundaries[0] == -1) {
      boundaries[0] = getMapBoundary(-1, 0);
    }

    if (boundaries[1] == -1) {
      boundaries[1] = getMapBoundary(1, 0);
    }

    if (boundaries[2] == -1) {
      boundaries[2] = getMapBoundary(0, -1);
    }

    if (boundaries[3] == -1) {
      boundaries[3] = getMapBoundary(0, 1);
    }
  }

  private int getMapBoundary(int dx, int dy) throws GameActionException {
    MapLocation location = rc.getLocation();
    int limit = (int) Math.floor(Math.sqrt(me.sensorRadiusSquared));

    for (int i = 1; i <= limit; i++) {
      location = location.translate(dx, dy);

      if (!rc.onTheMap(location)) {
        if (dx == 0) {
          return location.y - dy;
        } else {
          return location.x - dx;
        }
      }
    }

    return -1;
  }

  protected MapLocation senseRobot(RobotType robotType, Team team, int radiusSquared) {
    MapLocation closestRobot = null;
    int closestDistance = 0;

    for (RobotInfo robot : rc.senseNearbyRobots(radiusSquared, team)) {
      if (robot.getType() == robotType) {
        int distance = getDistanceTo(robot.getLocation());
        if (closestRobot == null || distance < closestDistance) {
          closestRobot = robot.getLocation();
          closestDistance = distance;
        }
      }
    }

    return closestRobot;
  }

  protected MapLocation senseRobot(RobotType robotType, Team team) {
    return senseRobot(robotType, team, -1);
  }

  protected int getDistanceTo(MapLocation location) {
    return rc.getLocation().distanceSquaredTo(location);
  }

  protected void drawLine(MapLocation from, MapLocation to, Color color) {
    rc.setIndicatorLine(from, to, color.getR(), color.getG(), color.getB());
  }

  protected void drawLine(MapLocation to, Color color) {
    drawLine(rc.getLocation(), to, color);
  }

  protected void drawDot(MapLocation location, Color color) {
    rc.setIndicatorDot(location, color.getR(), color.getG(), color.getB());
  }
}
