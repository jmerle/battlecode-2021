package camel_case_v6_sprint_1.robot;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;
import camel_case_v6_sprint_1.util.Color;
import camel_case_v6_sprint_1.util.FlagType;
import camel_case_v6_sprint_1.util.MapInfo;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class Robot {
  private static final FlagType[] FLAG_TYPES = FlagType.values();

  protected final RobotController rc;

  protected final RobotType me;
  private final boolean checkForMapBoundaries;

  protected Team myTeam;
  protected Team enemyTeam;

  protected MapInfo mapInfo = null;

  protected int[] boundaries = {-1, -1, -1, -1};

  protected int[] flags = new int[FlagType.values().length];
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

  protected Set<Integer> knownSlanderers = new HashSet<>();

  public Robot(RobotController rc, RobotType type, boolean checkForMapBoundaries) {
    this.rc = rc;
    this.checkForMapBoundaries = checkForMapBoundaries;

    me = type;

    myTeam = rc.getTeam();
    enemyTeam = myTeam.opponent();

    Arrays.fill(flags, -1);
  }

  public void run() throws GameActionException {
    for (RobotInfo robot : rc.senseNearbyRobots(me.detectionRadiusSquared, myTeam)) {
      parseFlag(robot.getID());
    }

    if (mapInfo == null) {
      updateMapInfo();
    }

    if (mapInfo == null && checkForMapBoundaries) {
      detectMapBoundaries();
      updateMapInfo();
    }
  }

  public void updateFlag() throws GameActionException {
    int currentFlag = rc.getFlag(rc.getID());

    for (int i = 0; i < flags.length; i++) {
      int index = (currentFlagIndex + i) % flags.length;

      if (flags[index] == -1) {
        continue;
      }

      int type = me == RobotType.SLANDERER ? 2 : 1;
      int newFlag = Integer.parseInt("" + type + index + flags[index]);

      if (currentFlag == 0 || newFlag != currentFlag) {
        rc.setFlag(newFlag);
        currentFlagIndex = index;
        break;
      }
    }
  }

  protected void parseFlag(int robotId) throws GameActionException {
    int flag = rc.getFlag(robotId);

    if (flag == 0) {
      return;
    }

    String flagStr = Integer.toString(flag);

    boolean isSlanderer = flagStr.charAt(0) == '2';
    int typeIndex = Integer.parseInt(flagStr.substring(1, 2));
    int value = Integer.parseInt(flagStr.substring(2));

    if (typeIndex >= FLAG_TYPES.length) {
      return;
    }

    if (isSlanderer) {
      knownSlanderers.add(robotId);
    } else {
      knownSlanderers.remove(robotId);
    }

    onFlag(FLAG_TYPES[typeIndex], value);
  }

  protected void onFlag(FlagType type, int value) {
    switch (type) {
      case MIN_X:
        boundaries[0] = value;
        setFlag(type, value);
        break;
      case MAX_X:
        boundaries[1] = value;
        setFlag(type, value);
        break;
      case MIN_Y:
        boundaries[2] = value;
        setFlag(type, value);
        break;
      case MAX_Y:
        boundaries[3] = value;
        setFlag(type, value);
        break;
    }
  }

  protected void setFlag(FlagType type, int value) {
    flags[type.ordinal()] = value;
  }

  protected void updateMapInfo() {
    if (mapInfo != null) {
      return;
    }

    mapInfo = MapInfo.parseBoundaries(boundaries[0], boundaries[1], boundaries[2], boundaries[3]);

    if (mapInfo != null) {
      setFlag(FlagType.MIN_X, mapInfo.minX);
      setFlag(FlagType.MAX_X, mapInfo.maxX);
      setFlag(FlagType.MIN_Y, mapInfo.minY);
      setFlag(FlagType.MAX_Y, -1);
    }
  }

  private void detectMapBoundaries() throws GameActionException {
    if (boundaries[0] == -1) {
      boundaries[0] = getMapBoundary(-1, 0);
      setFlag(FlagType.MIN_X, boundaries[0]);
    }

    if (boundaries[1] == -1) {
      boundaries[1] = getMapBoundary(1, 0);
      setFlag(FlagType.MAX_X, boundaries[1]);
    }

    if (boundaries[2] == -1) {
      boundaries[2] = getMapBoundary(0, -1);
      setFlag(FlagType.MIN_Y, boundaries[2]);
    }

    if (boundaries[3] == -1) {
      boundaries[3] = getMapBoundary(0, 1);
      setFlag(FlagType.MAX_Y, boundaries[3]);
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
    rc.setIndicatorLine(from, to, color.r, color.g, color.b);
  }

  protected void drawLine(MapLocation to, Color color) {
    drawLine(rc.getLocation(), to, color);
  }

  protected void drawDot(MapLocation location, Color color) {
    rc.setIndicatorDot(location, color.r, color.g, color.b);
  }
}
