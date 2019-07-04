const NEW_DATE_FORMAT = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}$/;
const OLD_DATE_FORMAT = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}\.\d{6}\+\d{2}:\d{2}$/;

/**
 * Taken from https://stackoverflow.com/a/1353711
 * @param {Date} d
 */
function isValidDate(d) {
  return d instanceof Date && !isNaN(d);
}

/**
 * Returns a clean measurement object with parsed lat/lon and p10/p25 values.
 * Returns undefined if the measurement included invalid/bad data
 * @param {Object} aMeasurement The raw measurement object (might include invalid data)
 * @param {string} aDatasetUrl The source of the data where the measurement is included
 */
function enrichMeasurement(aMeasurement, aDatasetUrl) {
  const P1parsed = parseFloat(aMeasurement.P1);
  if (Number.isNaN(P1parsed) || P1parsed < 0) return undefined;

  const P2parsed = parseFloat(aMeasurement.P2);
  if (Number.isNaN(P2parsed) || P1parsed < 0) return undefined;

  const latParsed = parseFloat(aMeasurement.lat);
  if (Number.isNaN(latParsed)) return undefined;

  const lonParsed = parseFloat(aMeasurement.lon);
  if (Number.isNaN(lonParsed)) return undefined;

  const result = Object.create(null);

  result.P10 = P1parsed;
  result.P25 = P2parsed;
  result.sensor_type = aMeasurement.sensor_type;
  result.sensor_id = aMeasurement.sensor_id;
  if (NEW_DATE_FORMAT.test(aMeasurement.timestamp)) {
    result.timestamp = new Date(`${aMeasurement.timestamp}Z`);
  } else if (OLD_DATE_FORMAT.test(aMeasurement.timestamp)) {
    result.timestamp = new Date(aMeasurement.timestamp);
  } else {
    // unrecognized date format, throw the measurement away
    console.log('WE NEED TO FIX THIS:');
    console.log(aMeasurement.timestamp);
    return undefined;
  }

  if (!isValidDate(result.timestamp)) {
    // something went wrong while parsing the date, throw it out
    console.log(`Invalid date ${result.timestamp} in ${aDatasetUrl}!`);
    return undefined;
  }
  result.lat = latParsed;
  result.lon = lonParsed;
  result.fromDataset = aDatasetUrl;

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
