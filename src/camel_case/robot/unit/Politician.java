package camel_case.robot.unit;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import camel_case.util.MapInfo;

public class Politician extends Unit {
  private boolean canDefend = true;

  public Politician(RobotController rc) {
    super(rc, RobotType.POLITICIAN, true);
  }

  public Politician(RobotController rc, MapLocation hq, MapInfo mapInfo, int[] boundaries) {
    this(rc);

    this.hq = hq;
    this.mapInfo = mapInfo;
    this.boundaries = boundaries;

    canDefend = false;
  }

  @Override
  public void run() throws GameActionException {
    super.run();

    RobotInfo empowerTarget = getClosestEmpowerTarget();

    if (empowerTarget != null) {
      int empowerTargetDistance = getDistanceTo(empowerTarget.getLocation());
      int maxTargetDistance =
          empowerTarget.getType() == RobotType.ENLIGHTENMENT_CENTER ? 2 : me.actionRadiusSquared;

      if (empowerTargetDistance <= maxTargetDistance) {
        if (tryEmpower(empowerTargetDistance)) {
          return;
        }
      }

      if (tryMoveTo(empowerTarget.getLocation())) {
        return;
      }
    }

    if (rc.getConviction() <= 10) {
      if (tryEmpower(me.actionRadiusSquared)) {
        return;
      }
    }

    if (isDefending()) {
      tryHide();
    } else {
      tryWander();
    }
  }

  private RobotInfo getClosestEmpowerTarget() {
    RobotInfo closestTarget = null;
    int closestDistance = -1;

    MapLocation closestHq = senseRobot(RobotType.ENLIGHTENMENT_CENTER, myTeam);
    if (closestHq == null) {
      closestHq = hq;
    }

    RobotInfo[] nearbyRobots = rc.senseNearbyRobots();

    boolean slandererNearby = false;
    for (RobotInfo robot : nearbyRobots) {
      if (robot.team == myTeam
          && robot.type == RobotType.POLITICIAN
          && knownSlanderers.contains(robot.ID)) {
        slandererNearby = true;
        break;
      }
    }

    int empowerFactor = (int) Math.floor(rc.getEmpowerFactor(myTeam, 0));

    for (RobotInfo robot : nearbyRobots) {
      if (robot.team == myTeam
          && !(robot.type == RobotType.ENLIGHTENMENT_CENTER && empowerFactor > 1000)) {
        continue;
      }

      if (robot.type == RobotType.MUCKRAKER && !isDefending() && !slandererNearby) {
        continue;
      }

      if (robot.type == RobotType.POLITICIAN
          && !isDefending()
          && robot.conviction * 5 < rc.getConviction() * empowerFactor
          && closestHq != null
          && closestHq.distanceSquaredTo(robot.location) > 40) {
        continue;
      }

      int distance = getDistanceTo(robot.location);
      if (closestTarget == null || distance < closestDistance) {
        closestTarget = robot;
        closestDistance = distance;
      }
    }

    return closestTarget;
  }

  private boolean tryEmpower(int radiusSquared) throws GameActionException {
    if (rc.canEmpower(radiusSquared)) {
      rc.empower(radiusSquared);
      return true;
    }

    return false;
  }

  private boolean isDefending() {
    return canDefend && rc.getID() % 4 == 0;
  }
}
