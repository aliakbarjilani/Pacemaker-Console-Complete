package controllers;

import static org.junit.Assert.*;
import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import models.Activity;
import models.User;
import static utils.TimeFormatters.parseDateTime;
import static utils.TimeFormatters.parseDuration;

import utils.JSONSerializer;
import utils.Serializer;
import utils.XMLSerializer;
import static models.Fixtures.*;

public class PersistenceTest {

  PacemakerAPI pacemaker;
  Serializer xmlSerializer;

  void deleteFile(String fileName) {
    File datastore = new File(fileName);
    if (datastore.exists()) {
      datastore.delete();
    }
  }

  void populate(PacemakerAPI pacemaker) {
    users.forEach(
        user -> pacemaker.createUser(user.firstName, user.lastName, user.email, user.password));

    User marge = pacemaker.getUserByEmail("marge@simpson.com");
    margeActivities.forEach(activity -> {
      Activity newActivity = pacemaker.createActivity(marge.id, activity.type, activity.location,
          activity.distance, parseDateTime(activity.starttime), parseDuration(activity.duration));
      route1.forEach(
          location -> pacemaker.addLocation(newActivity.id, location.latitude, location.longitude));
    });

    User lisa = pacemaker.getUserByEmail("lisa@simpson.com");
    lisasActivities.forEach(activity -> {
      Activity newActivity = pacemaker.createActivity(lisa.id, activity.type, activity.location,
          activity.distance, parseDateTime(activity.starttime), parseDuration(activity.duration));
      route2.forEach(
          location -> pacemaker.addLocation(newActivity.id, location.latitude, location.longitude));
    });
  }

  @Before
  public void setup() {
    deleteFile("datastore.xml");
    xmlSerializer = new XMLSerializer(new File("datastore.xml"));
    deleteFile("datastore.json");
    pacemaker = new PacemakerAPI(null);
    populate(pacemaker);
  }

  @After
  public void tearDown() {
    pacemaker = null;
    xmlSerializer = null;
  }

  @Test
  public void testXMLSerializer() throws Exception {
    pacemaker.serializer = xmlSerializer;
    pacemaker.store();
    PacemakerAPI pacemaker2 = new PacemakerAPI(null);
    pacemaker2.serializer = xmlSerializer;
    pacemaker2.load();
    pacemaker.getUsers().forEach(user -> assertTrue(pacemaker2.getUsers().contains(user)));
  }
  
  @Test
  public void testJSONSerializer() throws Exception {
    Serializer jsonSerializer = new JSONSerializer(new File("datastore.json"));
    pacemaker.serializer = jsonSerializer;
    pacemaker.store();
    PacemakerAPI pacemaker2 = new PacemakerAPI(null);
    pacemaker2.serializer = jsonSerializer;
    pacemaker2.load();
    pacemaker.getUsers().forEach(user -> assertTrue(pacemaker2.getUsers().contains(user)));
  }
  
}