package camel_case.robot.building;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import camel_case.robot.Robot;

public class EnlightenmentCenter extends Robot {
  private RobotType[] spawnOrder = {RobotType.MUCKRAKER, RobotType.SLANDERER, RobotType.POLITICIAN};
  private int nextSpawnIndex = 0;

  public EnlightenmentCenter(RobotController rc) {
    super(rc, RobotType.ENLIGHTENMENT_CENTER);
  }

  @Override
  public void run() throws GameActionException {
    for (Direction direction : adjacentDirections) {
      if (tryBuildRobot(spawnOrder[nextSpawnIndex], direction, 50)) {
        nextSpawnIndex = (nextSpawnIndex + 1) % spawnOrder.length;
        return;
      }
    }

    tryBid(1);
  }

  private boolean tryBid(int influence) throws GameActionException {
    if (rc.canBid(influence)) {
      rc.bid(influence);
      return true;
    }

    return false;
  }

  private boolean tryBuildRobot(RobotType type, Direction direction, int influence)
      throws GameActionException {
    if (rc.canBuildRobot(type, direction, influence)) {
      rc.buildRobot(type, direction, influence);
      return true;
    }

    return false;
  }
}
