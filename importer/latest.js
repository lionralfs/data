const { downloadLatest } = require('./downloader');

function isP1(obj) {
  return obj.value_type === 'P1';
}

function isP2(obj) {
  return obj.value_type === 'P2';
}

/**
 * changes a measurement that comes from the `getLatest()` call
 * to look like it came from the archive
 * return `false` if the measurement is faulty or not wanted
 */
function restructure(measurement) {
  const result = Object.create(null);

  // throw away indoor sensors
  if (measurement.location.indoor !== 0) return false;

  const p1 = measurement.sensordatavalues.find(isP1);
  if (!p1) return false;

  const p2 = measurement.sensordatavalues.find(isP2);
  if (!p2) return false;

  result.P1 = p1.value;
  result.P2 = p2.value;
  result.timestamp = measurement.timestamp.replace(/ /, 'T');
  result.sensor_id = String(measurement.sensor.id);
  result.sensor_type = measurement.sensor.sensor_type.name;
  result.lat = measurement.location.latitude;
  result.lon = measurement.location.longitude;

  return result;
}

/**
 * Returns the measurements of the last 5 minutes
 */
async function getLatest() {
  const latest = await downloadLatest();

  return latest.map(restructure).filter(Boolean);
}

module.exports = {
  getLatest: getLatest
};
