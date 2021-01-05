package camel_case;

import battlecode.common.Clock;
import battlecode.common.RobotController;
import camel_case.robot.Robot;
import camel_case.robot.building.EnlightenmentCenter;
import camel_case.robot.unit.Muckraker;
import camel_case.robot.unit.Politician;
import camel_case.robot.unit.Slanderer;

@SuppressWarnings("unused")
public class RobotPlayer {
  public static void run(RobotController rc) {
    Robot robot = createRobot(rc);

    if (robot == null) {
      return;
    }

    //noinspection InfiniteLoopStatement
    while (true) {
      performTurn(rc, robot);
      Clock.yield();
    }
  }

  private static void performTurn(RobotController rc, Robot robot) {
    int startTurn = rc.getRoundNum();

    try {
      robot.run();
    } catch (Exception e) {
      System.out.println("Exception in robot #" + rc.getID() + " (" + rc.getType() + ")");
      e.printStackTrace();
    }

    if (rc.getRoundNum() > startTurn) {
      int limit = rc.getType().bytecodeLimit;
      System.out.println("Used too much bytecode, the limit is " + limit + "!");
    } else {
      notifyHighBytecodeUsage(rc);
    }
  }

  private static Robot createRobot(RobotController rc) {
    switch (rc.getType()) {
      case ENLIGHTENMENT_CENTER:
        return new EnlightenmentCenter(rc);
      case POLITICIAN:
        return new Politician(rc);
      case SLANDERER:
        return new Slanderer(rc);
      case MUCKRAKER:
        return new Muckraker(rc);
      default:
        System.out.println("Unknown robot type '" + rc.getType() + "'");
        return null;
    }
  }

  private static void notifyHighBytecodeUsage(RobotController rc) {
    int used = Clock.getBytecodeNum();
    int total = rc.getType().bytecodeLimit;
    double percentage = (double) used / (double) total * 100.0;

    if (percentage >= 90) {
      String format = "High bytecode usage!\n%s/%s (%s%%)";
      System.out.printf((format) + "%n", used, total, (int) Math.round(percentage));
    }
  }
}
