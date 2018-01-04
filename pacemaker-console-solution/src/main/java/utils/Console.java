package utils;

import java.util.Collection;
import models.Activity;
import models.User;

public class Console {

  public void println(String s) {
    System.out.println(s);
  }
  public void renderUser(User user) {
    System.out.println(user.toString());
  }

  public void renderUsers(Collection<User> users) {
    System.out.println(users.toString());
  }

  public void renderActivity(Activity activities) {
    System.out.println(activities.toString());
  }

  public void renderActivities(Collection<Activity> activities) {
    System.out.println(activities.toString());
  }
}