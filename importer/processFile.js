const fetch = require('node-fetch');
const parse = require('csv-parse');

/**
 * Download a single CSV file, parse it into a JSON format and add a unique id
 */
module.exports = url => {
  // console.log(`Fetching ${url}`);
  return new Promise((resolve, reject) => {
    fetch(url)
      .then(res => res.text())
      .then(csvContent => {
        parse(
          csvContent,
          {
            delimiter: ';',
            columns: true,
            skip_empty_lines: true
          },
          (err, records) => {
            try {
              if (err) return reject(err.message);

              const recordsWithId = records
                .filter(el => el.P1 !== '' && el.P2 !== '')
                .map(measurement => ({
                  ...measurement,
                  P1: parseFloat(measurement.P1),
                  P2: parseFloat(measurement.P2),
                  timestamp: new Date(measurement.timestamp),
                  lat: parseFloat(measurement.lat),
                  lon: parseFloat(measurement.lon),
                  _id: `${measurement.sensor_id}-${measurement.sensor_type}-${measurement.timestamp}`
                }));

              return resolve(recordsWithId);
            } catch (err) {
              reject(err);
            }
          }
        );
      })
      .catch(reject);
  });
};
