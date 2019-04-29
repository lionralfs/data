package airDataBackendService.database;

import java.util.List;

import airDataBackendService.util.Box;

public interface MeasurementRepositoryCustom {
  public List<Measurement> customQuery(int limit, int offset, Box box);
}