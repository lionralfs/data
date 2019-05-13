const fetch = require('node-fetch');
const { downloadFromArchive } = require('./downloader');
const { getCSVFiles } = require('./getCSVFiles');
const { chunkArray } = require('./utils');
const { processFile } = require('./processFile');
const { connectToCollection } = require('./database');
const { zeroPad } = require('./utils');
const { parseCSV } = require('./utils');

// uh oh, global state
let addedMeasurements = 0;

function createFileProcessor(dateString, dbCollection) {
  return function(file) {
    const url = `https://archive.luftdaten.info/${dateString}/${file}`;

    return new Promise(async resolve => {
      try {
        // download the file
        const response = await fetch(url);
        // read the response text
        const rawText = await response.text();
        // parse it into a JS object
        const measurements = await parseCSV(rawText, {
          delimiter: ';',
          columns: true,
          skip_empty_lines: true
        });
        const cleanMeasurements = await processFile(measurements, url);

        dbCollection
          .insertMany(cleanMeasurements, { ordered: false })
          .then(res => {
            addedMeasurements += res.insertedCount;
            // console.log(`Written to DB: ${res.insertedCount}`);
            resolve();
          })
          .catch(err => {
            console.log(`Got "${err.message}" when trying to write ${url} into database`);
          });
      } catch (err) {
        console.error(`An error occured while processing ${url}:\n${err}\nNotice: this did not stop the processing of the current set of CSV files\n\n`);
      }

      resolve();
    });
  };
}

/**
 * Returns a function, which, when called, returns a promise that
 * resolves as soon as all files in the chunk have been processed
 */
function chunkToFunction(chunk, processFileFn) {
  return function() {
    const processes = chunk.map(processFileFn);
    return Promise.all(processes);
  };
}

function processSequentially(promises) {
  const last = promises.reduce(async (prevPromise, nextFn) => {
    await prevPromise;
    return nextFn();
  }, Promise.resolve());

  return last;
}

async function getEntireDay(dateString) {
  const html = await downloadFromArchive(dateString);
  const csvFiles = getCSVFiles(html);
  const fileChunks = chunkArray(csvFiles, 50);

  // open db connection
  const [client, collection] = await connectToCollection();
  const processFileFn = createFileProcessor(dateString, collection);
  const chunkProcesses = fileChunks.map(chunk => chunkToFunction(chunk, processFileFn));

  // Send off 50 requests at once, then wait until they're done before starting the next 50
  await processSequentially(chunkProcesses);

  client.close();
}

/**
 * If today is 27.04.2019, the following snippet will turn the current date into a
 * formatted string, representing yesterday: "2019-04-26", because it is the date format
 * used by https://archive.luftdaten.info. We need to use yesterdays date because todays
 * dataset is not uploaded yet.
 */
const today = new Date();
const yesterday = new Date(new Date().setDate(today.getDate() - 1));
const yesterdayFormatted = `${yesterday.getFullYear()}-${zeroPad(yesterday.getMonth() + 1)}-${zeroPad(yesterday.getDate())}`;

addedMeasurements = 0;
console.log(`--- Downloading latest measurements from https://archive.luftdaten.info (${yesterdayFormatted}) ---`);
getEntireDay(yesterdayFormatted)
  .then(() => {
    console.log(`--- Added ${addedMeasurements} new measurements to the database ---`);
    console.log(`--- Total time: ${Math.floor((new Date().getTime() - today.getTime()) / 1000)} seconds---`);
  })
  .catch(console.error);
