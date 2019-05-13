const parse = require('csv-parse');

function parseCSV(textContent, options) {
  return new Promise((resolve, reject) => {
    parse(textContent, options, function(error, records) {
      if (error) {
        reject(error);
        return;
      }
      resolve(records);
    });
  });
}

module.exports = {
  zeroPad: num => (num > 9 ? `${num}` : `0${num}`),
  chunkArray: (array, chunk_size) =>
    Array(Math.ceil(array.length / chunk_size))
      .fill()
      .map((_, index) => index * chunk_size)
      .map(begin => array.slice(begin, begin + chunk_size)),
  parseCSV: parseCSV
};
