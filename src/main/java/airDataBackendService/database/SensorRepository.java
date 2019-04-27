package airDataBackendService.database;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<SensorDataForDatabase, Long> {

}
