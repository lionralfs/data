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

              const recordsWithId = [];

              records.forEach(measurement => {
                const P1parsed = parseFloat(measurement.P1);
                const P2parsed = parseFloat(measurement.P2);

                if (Number.isNaN(P1parsed)) return;
                if (Number.isNaN(P2parsed)) return;

                recordsWithId.push({
                  ...measurement,
                  P10: P1parsed,
                  P25: P2parsed,
                  timestamp: new Date(measurement.timestamp + 'Z'),
                  lat: parseFloat(measurement.lat),
                  lon: parseFloat(measurement.lon),
                  fromDataset: url,
                  _id: `${measurement.sensor_id}-${measurement.sensor_type}-${measurement.timestamp}`
                });
              });

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
