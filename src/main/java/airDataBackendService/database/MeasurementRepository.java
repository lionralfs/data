package airDataBackendService.database;

import org.springframework.data.mongodb.repository.MongoRepository;
import airDataBackendService.database.Measurement;

public interface MeasurementRepository extends MongoRepository<Measurement, String>, MeasurementRepositoryCustom {
}