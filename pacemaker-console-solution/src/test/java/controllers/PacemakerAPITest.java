package controllers;

import static org.junit.Assert.*;

import java.util.List;
import org.joda.time.DateTimeComparator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.Activity;
import models.Location;
import models.User;

import static utils.TimeFormatters.parseDateTime;
import static utils.TimeFormatters.parseDuration;

import static models.Fixtures.users;
import static models.Fixtures.activities;
import static models.Fixtures.locations;
import static models.Fixtures.margeActivities;

public class PacemakerAPITest {

  private PacemakerAPI pacemaker;

  void assertEquivalent(List<Activity> list1, List<Activity> list2) {
    assertEquals(list1.size(), list1.size());
    int index = 0;
    for (Activity activity : list1) {
      assertEquals(activity.location, list2.get(index).location);
      index++;
    }
  }

  @Before
  public void setup() {
    pacemaker = new PacemakerAPI(null);
    users.forEach(
        user -> pacemaker.createUser(user.firstName, user.lastName, user.email, user.password));
  }

  @After
  public void tearDown() {
    pacemaker = null;
  }

  @Test
  public void testUser() {
    assertEquals(users.size(), pacemaker.getUsers().size());
    pacemaker.createUser("homer", "simpson", "homer@simpson.com", "secret");
    assertEquals(users.size() + 1, pacemaker.getUsers().size());
    assertEquals(users.get(0), pacemaker.getUserByEmail(users.get(0).email));
  }

  @Test
  public void testEquals() {
    User homer = new User("homer", "simpson", "homer@simpson.com", "secret");
    User homer2 = new User("homer", "simpson", "homer@simpson.com", "secret");
    User bart = new User("bart", "simpson", "bartr@simpson.com", "secret");

    assertEquals(homer, homer);
    assertEquals(homer, homer2);
    assertNotEquals(homer, bart);

    assertSame(homer, homer);
    assertNotSame(homer, homer2);
  }

  @Test
  public void testUsers() {
    assertEquals(users.size(), pacemaker.getUsers().size());
    users.forEach(user -> {
      User eachUser = pacemaker.getUserByEmail(user.email);
      assertEquals(user, eachUser);
      assertNotSame(user, eachUser);
    });
  }

  @Test
  public void testDeleteUsers() {
    assertEquals(users.size(), pacemaker.getUsers().size());
    User marge = pacemaker.getUserByEmail("marge@simpson.com");
    pacemaker.deleteUser(marge.id);
    assertEquals(users.size() - 1, pacemaker.getUsers().size());
  }

  @Test
  public void testAddActivity() {
    User marge = pacemaker.getUserByEmail("marge@simpson.com");
    Activity testActivity = margeActivities.get(0);
    Activity activity = pacemaker
        .createActivity(marge.id, testActivity.type, testActivity.location, testActivity.distance,
            parseDateTime(testActivity.starttime), parseDuration(testActivity.duration));
    Activity returnedActivity = pacemaker.getActivity(activity.id);
    assertEquals(activities.get(0), returnedActivity);
    assertNotSame(activities.get(0), returnedActivity);
  }

  @Test
  public void testAddActivityWithSingleLocation() {
    User marge = pacemaker.getUserByEmail("marge@simpson.com");
    Activity testActivity = margeActivities.get(0);
    String activityId = pacemaker
        .createActivity(marge.id, testActivity.type, testActivity.location, testActivity.distance,
            parseDateTime(testActivity.starttime), parseDuration(testActivity.duration)).id;

    pacemaker.addLocation(activityId, locations.get(0).latitude, locations.get(0).longitude);

    Activity activity = pacemaker.getActivity(activityId);
    assertEquals(1, activity.route.size());
    assertEquals(0.0001, locations.get(0).latitude, activity.route.get(0).latitude);
    assertEquals(0.0001, locations.get(0).longitude, activity.route.get(0).longitude);
  }

  @Test
  public void testAddActivityWithMultipleLocation() {
    User marge = pacemaker.getUserByEmail("marge@simpson.com");
    Activity testActivity = margeActivities.get(0);
    String activityId = pacemaker
        .createActivity(marge.id, testActivity.type, testActivity.location, testActivity.distance,
            parseDateTime(testActivity.starttime), parseDuration(testActivity.duration)).id;

    locations.forEach(
        location -> pacemaker.addLocation(activityId, location.latitude, location.longitude));

    Activity activity = pacemaker.getActivity(activityId);
    assertEquals(locations.size(), activity.route.size());

    int i = 0;
    for (Location location : activity.route) {
      assertEquals(location, locations.get(i));
      i++;
    }
  }

  @Test
  public void testSortedReports() {
    User homer = pacemaker.createUser("homer", "simpson", "homer@simpson.com", "secret");
    activities.forEach(
        activity -> pacemaker
            .createActivity(homer.id, activity.type, activity.location, activity.distance,
                parseDateTime(activity.starttime), parseDuration(activity.duration))
    );
    List<Activity> activities = pacemaker.listActivities(homer.id, "type");

    for (int i = 0; i < activities.size() - 1; i++) {
      assertTrue(activities.get(i).type.compareTo(activities.get(i + 1).type) <= 0);
    }
    activities = pacemaker.listActivities(homer.id, "location");
    for (int i = 0; i < activities.size() - 1; i++) {
      assertTrue(activities.get(i).location.compareTo(activities.get(i + 1).location) <= 0);
    }
    activities = pacemaker.listActivities(homer.id, "distance");
    for (int i = 0; i < activities.size() - 1; i++) {
      assertTrue(activities.get(i).distance <= activities.get(i + 1).distance);
    }
    activities = pacemaker.listActivities(homer.id, "date");
    for (int i = 0; i < activities.size() - 1; i++) {
      assertTrue( DateTimeComparator.getInstance().compare(activities.get(i).starttime, activities.get(i+1).starttime) <= 0);
    }
    activities = pacemaker.listActivities(homer.id, "duration");
    for (int i = 0; i < activities.size() - 1; i++) {
      assertTrue( activities.get(i).duration.getStandardSeconds() <  activities.get(i+1).duration.getStandardSeconds());
    }
  }
}