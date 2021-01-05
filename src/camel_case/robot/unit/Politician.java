package camel_case.robot.unit;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class Politician extends Unit {
  public Politician(RobotController rc) {
    super(rc, RobotType.POLITICIAN);
  }

  @Override
  public void run() throws GameActionException {
    RobotInfo empowerTarget = getClosestEmpowerTarget();

    if (empowerTarget != null) {
      int empowerTargetDistance = empowerTarget.getLocation().distanceSquaredTo(rc.getLocation());
      if (empowerTargetDistance <= me.actionRadiusSquared) {
        if (tryEmpower(me.actionRadiusSquared)) {
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

    tryMoveRandom();
  }

  private RobotInfo getClosestEmpowerTarget() {
    RobotInfo closestTarget = null;
    int closestDistance = -1;

    for (RobotInfo robot : rc.senseNearbyRobots()) {
      if (robot.getTeam() == myTeam) {
        continue;
      }

      int distance = robot.getLocation().distanceSquaredTo(rc.getLocation());
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
