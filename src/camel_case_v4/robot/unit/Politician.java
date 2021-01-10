package camel_case_v4.robot.unit;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import camel_case_v4.util.MapInfo;

public class Politician extends Unit {
  public Politician(RobotController rc) {
    super(rc, RobotType.POLITICIAN, true);
  }

  public Politician(RobotController rc, MapLocation hq, MapInfo mapInfo, int[] boundaries) {
    this(rc);

    this.hq = hq;
    this.mapInfo = mapInfo;
    this.boundaries = boundaries;
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

    if (rc.getID() % 6 == 0) {
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

    for (RobotInfo robot : rc.senseNearbyRobots()) {
      if (robot.team == myTeam) {
        continue;
      }

      if (robot.type == RobotType.POLITICIAN
          && robot.influence * 5 < rc.getInfluence()
          && closestHq != null
          && closestHq.distanceSquaredTo(robot.location) > 100) {
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
}
