package airDataBackendService.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import airDataBackendService.database.DailyMeasurement;
import airDataBackendService.database.Measurement;
import airDataBackendService.util.Box;

public class MeasurementRepositoryCustomImpl implements MeasurementRepositoryCustom {
  @Autowired
  MongoTemplate mongoTemplate;

  @Override
  public List<Measurement> customQuery(int limit, int offset, Box box, Date maxage) {
    List<AggregationOperation> operations = new ArrayList<AggregationOperation>();

    operations.add(Aggregation.sort(Direction.ASC, "timestamp"));
    operations.add(Aggregation.match(Criteria.where("timestamp").gte(maxage)));

    if (box != null) {
      operations.add(Aggregation.match(
          new Criteria().andOperator(Criteria.where("lat").gte(box.getLat1()), Criteria.where("lat").lte(box.getLat2()),
              Criteria.where("lon").gte(box.getLon1()), Criteria.where("lon").lte(box.getLon2()))));
    }

    // operations.add(Aggregation.group("sensorId").avg("p25").as("P25"));

    operations.add(Aggregation.skip((long) offset));
    operations.add(Aggregation.limit(limit));

    Aggregation agg = Aggregation.newAggregation(operations);

    return mongoTemplate.aggregate(agg, Measurement.class, Measurement.class).getMappedResults();
  }

  @Override
  public List<Measurement> getBySensor(String sensor, long timestamp) {
    int threshold = 10 * 60; // 10min represented in seconds
    long from = (timestamp) - threshold;
    long to = (timestamp) + threshold;

    List<Measurement> dailyMeasurements = this.getBySensorFullDay(sensor, timestamp);

    // TODO: check previous day
    // System.out.println(((long) timestamp * 1000) - calendar.getTimeInMillis() <
    // threshold * 1000);
    // TODO: check next day

    List<Measurement> result = new ArrayList<Measurement>(0);

    for (Measurement m : dailyMeasurements) {
      if (m.timestamp >= from && m.timestamp <= to) {
        result.add(m);
      }
    }

    return result;
  }

  private Date timestampToDay(long timestamp) {
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    calendar.setTimeInMillis((long) timestamp * 1000);
    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0,
        0);

    return calendar.getTime();
  }

  @Override
  public List<Measurement> getBySensorFullDay(String sensor, long timestamp) {
    List<Measurement> result = new ArrayList<Measurement>();

    Query query = new Query(Criteria.where("sensor_id").is(sensor).and("day").is(this.timestampToDay(timestamp)));
    DailyMeasurement exactDay = mongoTemplate.findOne(query, DailyMeasurement.class);

    if (exactDay == null) {
      return new ArrayList<Measurement>(0);
    }

    return result;
  }

  @Override
  public List<DailyMeasurement> getMeasurementsByDay(long timestamp) {
    Date day = this.timestampToDay(timestamp);
    Query query = new Query(Criteria.where("day").is(day));

    List<DailyMeasurement> result = mongoTemplate.find(query, DailyMeasurement.class, "sensordata");

    if (result == null) {
      return new ArrayList<DailyMeasurement>(0);
    }

    return result;
  }
}
