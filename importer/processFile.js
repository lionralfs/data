/**
 * Returns a clean measurement object with parsed lat/lon and p10/p25 values.
 * Returns undefined if the measurement included invalid/bad data
 * @param {Object} measurement The raw measurement object (might include invalid data)
 * @param {string} datasetUrl The source of the data where the measurement is included
 */
function modifyMeasurement(measurement, datasetUrl) {
  const P1parsed = parseFloat(measurement.P1);
  if (Number.isNaN(P1parsed)) return undefined;

  const P2parsed = parseFloat(measurement.P2);
  if (Number.isNaN(P2parsed)) return undefined;

  const latParsed = parseFloat(measurement.lat);
  if (Number.isNaN(latParsed)) return undefined;

  const lonParsed = parseFloat(measurement.lon);
  if (Number.isNaN(lonParsed)) return undefined;

  const result = Object.create(null);

  result.P10 = P1parsed;
  result.P25 = P2parsed;
  result.sensor_type = measurement.sensor_type;
  result.sensor_id = measurement.sensor_id;
  result.timestamp = new Date(`${measurement.timestamp}Z`);
  result.lat = latParsed;
  result.lon = lonParsed;
  result.fromDataset = datasetUrl;
  result._id = `${measurement.sensor_id}-${measurement.sensor_type}-${measurement.timestamp}`;

  return result;
}

/**
 * @param {Array} measurements
 * @param {string} url The URL of the CSV file to be processed
 * @throws
 */
async function processFile(measurements, url) {
  const result = [];

  for (const measurement of measurements) {
    const modified = modifyMeasurement(measurement, url);
    if (modified === undefined) {
      continue;
    }
    result.push(modified);
  }

  return result;
}

module.exports = {
  processFile: processFile
};
