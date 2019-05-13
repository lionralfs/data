const { getLatest } = require('./latest');
const { connectToCollection } = require('./database');
const { processFile } = require('./processFile');

const now = new Date();

console.log(`--- Downloading latest measurements from API ---`);
getLatest()
  .then(async latest => {
    const [client, collection] = await connectToCollection();

    const cleanMeasurements = await processFile(latest, `latest-${now}`);

    try {
      await collection.insertMany(cleanMeasurements, { ordered: false });
    } catch (err) {
      let duplicatedIds = 0;
      err.writeErrors.forEach(error => {
        if (error.code === 11000) {
          duplicatedIds++;
        } else {
          console.log(`    Got "${error.errmsg}" when trying to write latest into database`);
        }
      });
      console.log(`    Duplicated Ids: ${duplicatedIds} from ${cleanMeasurements.length} measurements.`);
    }
    console.log(`--- Total time: ${Math.floor((new Date().getTime() - now.getTime()) / 1000)} seconds---`);
    client.close();
  })
  .catch(console.error);
