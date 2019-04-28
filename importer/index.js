const { MongoClient } = require('mongodb');
const fetch = require('node-fetch');
const getCSVFiles = require('./getCSVFiles');
const { zeroPad } = require('./utils');
const processFile = require('./processFile');

// Connection URL
const url = 'mongodb://localhost:27017';
// Database Client
const client = new MongoClient(url, { useNewUrlParser: true });
// Database Name
const dbName = 'airdata';

/**
 * If today is 27.04.2019, the following snippet will turn the current date into a
 * formatted string, representing yesterday: "2019-04-27", because it is the date format
 * used by https://archive.luftdaten.info. We need to use yesterdays date because todays
 * dataset is not uploaded yet.
 */
const today = new Date();
const yesterdayFormatted = `${today.getFullYear()}-${zeroPad(today.getMonth() + 1)}-${zeroPad(today.getDate() - 2)}`;

/**
 * Setup the downloader
 */
console.log(`--- Downloading latest measurements from https://archive.luftdaten.info (${yesterdayFormatted}) ---`);
fetch(`https://archive.luftdaten.info/${yesterdayFormatted}/`)
  .then(res => {
    if (res.status !== 200) {
      throw new Error(`Error while trying to get measurements from https://archive.luftdaten.info/${yesterdayFormatted}/\nStatus code: ${res.status}`);
    }
    return res.text();
  })
  .then(htmlString => {
    /**
     * Given the raw HTML page, try to extract the individual csv files as an array, such as:
     *
     * [
     * '2019-04-26_sds011_sensor_467.csv',
     * '2019-04-26_sds011_sensor_471.csv',
     * ...
     * ]
     *
     */
    const files = getCSVFiles(htmlString);
    if (!files.length) {
      throw new Error(`Something went wrong, got 0 CSV files from https://archive.luftdaten.info/${yesterdayFormatted}/`);
    }
    return files;
  })
  .then(files => {
    /**
     * Given the array of CSV files, attempt to download them,
     * read their content and write it into the database
     */
    // TODO: remove following line (only here for debugging purposes)
    files = files.slice(0, 10);

    // Connect to the mongodb server
    client.connect((err, client) => {
      if (err) {
        throw new Error(err.message);
      }

      const db = client.db(dbName);
      db.collection('measurements', (err, collection) => {
        if (err) {
          throw new Error(err.message);
        }

        const processes = [];
        let addedMeasurements = 0;
        files.forEach(file => {
          processes.push(
            new Promise(async resolve => {
              const url = `https://archive.luftdaten.info/${yesterdayFormatted}/${file}`;
              try {
                const records = await processFile(url);

                collection
                  .insertMany(records, { ordered: false })
                  .then(res => {
                    addedMeasurements += res.insertedCount;
                    resolve();
                  })
                  .catch(err => {
                    console.log(`Got "${err.message}" when trying to write ${url} into database`);
                    resolve();
                  });
              } catch (err) {
                console.error(`An error occured while processing ${url}:\n${err}\nNotice: this did not stop the processing of the current set of CSV files\n\n`);
                resolve();
              }
            })
          );
        });

        Promise.all(processes).then(() => {
          console.log(`--- Added ${addedMeasurements} new measurements to the database ---`);
          client.close();
        });
      });
    });
  })
  .catch(err => {
    // TODO: log error somewhere
    console.error(err.message);
  });
