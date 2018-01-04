package controllers;

import com.google.common.base.Optional;

import java.io.File;

import asg.cliche.Command;
import asg.cliche.Param;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import models.Activity;
import models.User;
import utils.AsciiTableParser;
import utils.Console;
import utils.JSONSerializer;

import utils.Serializer;
import utils.XMLSerializer;

public class PacemakerConsoleService {

  PacemakerAPI paceApi;
  File datastore = new File("datastore");
  Serializer xmlSerializer = new XMLSerializer(datastore);
  Serializer jsonSerializer = new JSONSerializer(datastore);
  Console console = new AsciiTableParser();

  public PacemakerConsoleService() throws Exception {
    paceApi = new PacemakerAPI(xmlSerializer);
    if (datastore.isFile()) {
      paceApi.load();
    }
  }

  @Command(description = "Create a new User")
  public void createUser(@Param(name = "first name") String firstName,
      @Param(name = "last name") String lastName,
      @Param(name = "email") String email, @Param(name = "password") String password) {
    console.renderUser(paceApi.createUser(firstName, lastName, email, password));
  }

  @Command(description = "Get a Users details")
  public void getUser(@Param(name = "email") String email) {
    console.renderUser(paceApi.getUserByEmail(email));
  }

  @Command(description = "Get all users details")
  public void getUsers() {
    console.renderUsers(paceApi.getUsers());
  }

  @Command(description = "Delete a User")
  public void deleteUser(@Param(name = "email") String email) {
    console.renderUser(paceApi.getUserByEmail(email));
  }

  @Command(description = "Add an activity")
  public void addActivity(@Param(name = "user-id") String id,
      @Param(name = "type") String type,
      @Param(name = "location") String location,
      @Param(name = "distance") double distance,
      @Param(name = "starttime") String starttime,
      @Param(name = "duration") String duration) {
    console
        .renderActivity(paceApi.createActivity(id, type, location, distance, starttime, duration));
  }

  @Command(description = "Add a location to an activity")
  public void addLocation(@Param(name = "activity-id") String id,
      @Param(name = "longitude") double longitude,
      @Param(name = "latitude") double latitude) {
    Optional<Activity> activity = Optional.fromNullable(paceApi.getActivity(id));
    if (activity.isPresent()) {
      paceApi.addLocation(activity.get().id, latitude, longitude);
      console.println("ok");
    } else {
      console.println("not found");
    }
  }

  @Command(description = "List a users activities")
  public void listActivities(@Param(name = "user id") String id) {
    Optional<User> user = Optional.fromNullable(paceApi.getUser(id));
    if (user.isPresent()) {
      console.renderActivities(paceApi.getActivities(user.get().id));
    }
  }

  @Command(description = "List all Activities")
  public void lisAllActivities(@Param(name = "userid") Long id,
      @Param(name = "sortBy: type, location, distance, date, duration") String sortBy) {
  }

  @Command(description="List Activities")
  public void listActivities (@Param(name="userid") String id, @Param(name="sortBy: type, location, distance, date, duration") String sortBy)
  {
    Set<String> options = new HashSet<>(Arrays.asList("type", "location", "distance", "date", "duration"));
    if (options.contains(sortBy))  {
      console.renderActivities(paceApi.listActivities(id, sortBy));
    }
    else
      console.println ("usage : la " + options.toString());
  }

  @Command(description = "Set file format")
  public void changeFileFormat(@Param(name = "file format: xml, json") String fileFormat) {
    switch (fileFormat) {
      case "xml":
        paceApi.serializer = xmlSerializer;
        break;
      case "json":
        paceApi.serializer = jsonSerializer;
        break;
    }
  }

  @Command(description = "Load activities persistent store")
  public void load() throws Exception {
    paceApi.load();
  }

  @Command(description = "Store activities persistent store")
  public void store() throws Exception {
    paceApi.store();
  }
}