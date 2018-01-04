package models;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import static models.Fixtures.users;

public class UserTest {

  User homer = new User("homer", "simpson", "homer@simpson.com", "secret");

  @Test
  public void testCreate() {
    assertEquals("homer", homer.firstName);
    assertEquals("simpson", homer.lastName);
    assertEquals("homer@simpson.com", homer.email);
    assertEquals("secret", homer.password);
  }

  @Test
  public void testIds() {
    Set<String> ids = new HashSet<>();
    for (User user : users) {
      ids.add(user.id);
    }
    assertEquals(users.size(), ids.size());
  }

  @Test
  public void testToString() {
    assertEquals("User{" + homer.id + ", homer, simpson, secret, homer@simpson.com, {}}",
        homer.toString());
  }


  @Test
  public void tesAddActivity() {
    Activity activity = new Activity("walk", "fridge", 0.001, "11:2:2012 9:00:00", "20:00:00");
    homer.activities.put(activity.id,activity);
    assertEquals("User{" + homer.id + ", homer, simpson, secret, homer@simpson.com, {" + activity.id + "=Activity{" + activity.id + ", walk, fridge, 0.001, 11:02:2012 09:00:00, 20:0:0, []}}}",
        homer.toString());
  }
}