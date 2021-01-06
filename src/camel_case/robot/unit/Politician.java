package camel_case.robot.unit;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class Politician extends Unit {
  public Politician(RobotController rc) {
    super(rc, RobotType.POLITICIAN, true);
  }

  @Override
  public void run() throws GameActionException {
    super.run();

    RobotInfo empowerTarget = getClosestEmpowerTarget();

    if (empowerTarget != null) {
      int empowerTargetDistance = getDistanceTo(empowerTarget.getLocation());
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

    tryWander();
  }

  private RobotInfo getClosestEmpowerTarget() {
    RobotInfo closestTarget = null;
    int closestDistance = -1;

    for (RobotInfo robot : rc.senseNearbyRobots()) {
      if (robot.getTeam() == myTeam || robot.getType() == RobotType.SLANDERER) {
        continue;
      }

      int distance = getDistanceTo(robot.getLocation());
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
