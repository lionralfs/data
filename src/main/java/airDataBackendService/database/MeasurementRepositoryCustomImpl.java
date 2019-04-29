package airDataBackendService.database;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import airDataBackendService.database.Measurement;
import airDataBackendService.util.Box;

public class MeasurementRepositoryCustomImpl implements MeasurementRepositoryCustom {
  @Autowired
  MongoTemplate mongoTemplate;

  @Override
  public List<Measurement> customQuery(int limit, int offset, Box box) {
    List<AggregationOperation> operations = new ArrayList<AggregationOperation>();

    System.out.println(box);

    operations.add(Aggregation.sort(Direction.ASC, "timestamp"));
    if (box != null) {
      operations.add(Aggregation.match(new Criteria().andOperator(
            Criteria.where("lat").gte(box.getLat1()),
            Criteria.where("lat").lte(box.getLat2()),
            Criteria.where("lon").gte(box.getLon1()),
            Criteria.where("lon").lte(box.getLon2()))
      ));
    }
    operations.add(Aggregation.skip((long) offset));
    operations.add(Aggregation.limit(limit));

    Aggregation agg = Aggregation.newAggregation(operations);

    return mongoTemplate.aggregate(agg, Measurement.class, Measurement.class).getMappedResults();
  }
}
