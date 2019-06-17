/**
 * Returns a clean measurement object with parsed lat/lon and p10/p25 values.
 * Returns undefined if the measurement included invalid/bad data
 * @param {Object} aMeasurement The raw measurement object (might include invalid data)
 * @param {string} aDatasetUrl The source of the data where the measurement is included
 */
function enrichMeasurement(aMeasurement, aDatasetUrl) {
  const P1parsed = parseFloat(aMeasurement.P1);
  if (Number.isNaN(P1parsed) || P1parsed < 0) return undefined;

  const P2parsed = parseFloat(aMeasurement.P2);
  if (Number.isNaN(P2parsed) || P1parsed < 0) return undefined;

  const latParsed = parseFloat(aMeasurement.lat);
  if (Number.isNaN(latParsed)) return undefined;

  const lonParsed = parseFloat(aMeasurement.lon);
  if (Number.isNaN(lonParsed)) return undefined;

  const result = Object.create(null);

  result.P10 = P1parsed;
  result.P25 = P2parsed;
  result.sensor_type = aMeasurement.sensor_type;
  result.sensor_id = aMeasurement.sensor_id;
  result.timestamp = new Date(`${aMeasurement.timestamp}Z`);
  result.lat = latParsed;
  result.lon = lonParsed;
  result.fromDataset = aDatasetUrl;
  result._id = `${aMeasurement.sensor_id}-${aMeasurement.sensor_type}-${aMeasurement.timestamp}`;

  return result;
}

/**
 * @param {Array} measurements
 * @param {string} aDatasetUrl Where the measurements came from
 * @throws
 */
async function cleanMeasurements(measurements, aDatasetUrl) {
  const result = [];

  for (const measurement of measurements) {
    const modified = enrichMeasurement(measurement, aDatasetUrl);
    if (modified === undefined) {
      continue;
    }
    result.push(modified);
  }

  return result;
}

module.exports = {
  cleanMeasurements: cleanMeasurements
};
