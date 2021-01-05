package camel_case.robot;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;
import camel_case.util.Color;

public abstract class Robot {
  protected RobotController rc;

  protected RobotType me;

  protected Team myTeam;
  protected Team enemyTeam;

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

  public Robot(RobotController rc, RobotType type) {
    this.rc = rc;

    me = type;

    myTeam = rc.getTeam();
    enemyTeam = myTeam.opponent();
  }

  public abstract void run() throws GameActionException;

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
