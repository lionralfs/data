package airDataBackendService.database;

import org.springframework.data.mongodb.repository.MongoRepository;

import airDataBackendService.database.Sensor;

public interface SensorRepository extends MongoRepository<Sensor, String> {
}