package models;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.common.base.Objects;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import static utils.TimeFormatters.parseDateTime;
import static utils.TimeFormatters.parseDuration;

public class Activity implements Serializable {

  public String id;
  public String type;
  public String location;
  public double distance;
  public DateTime starttime;
  public Duration duration;
  public List<Location> route = new ArrayList<>();

  public Activity() {
  }

  public Activity(String type, String location, double distance, String start, String duration) {
    this.id = UUID.randomUUID().toString();
    this.type = type;
    this.location = location;
    this.distance = distance;
    this.starttime = parseDateTime(start);
    this.duration = parseDuration(duration);
  }

  public String getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public String getLocation() {
    return location;
  }

  public String getDistance() {
    return Double.toString(distance);
  }

  public String getRoute() {
    return route.toString();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof Activity) {
      final Activity other = (Activity) obj;
      return Objects.equal(type, other.type)
          && Objects.equal(location, other.location)
          && Objects.equal(distance, other.distance)
          && Objects.equal(starttime, other.starttime)
          && Objects.equal(duration, other.duration)
          && Objects.equal(route, other.route);
    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    return toStringHelper(this).addValue(id)
        .addValue(type)
        .addValue(location)
        .addValue(distance)
        .addValue(parseDateTime(starttime))
        .addValue(parseDuration(duration))  
        .addValue(route)
        .toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.id, this.type, this.location, this.distance, this.starttime, this.duration);
  }
}