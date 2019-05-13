const { MongoClient } = require('mongodb');

// Connection URL
const url = 'mongodb://localhost:27017';
// Database Name
const dbName = 'airdata';

function connectToCollection() {
  return new Promise((resolve, reject) => {
    const client = new MongoClient(url, { useNewUrlParser: true });
    client.connect((err, client) => {
      if (err) {
        reject(err);
        return;
      }

      const db = client.db(dbName);

      db.collection('measurements', (err, collection) => {
        if (err) {
          reject(err);
          return;
        }

        // create an index for the "timestamp" field,
        // this makes mongodb maintain the ascending order
        // TODO: maybe move this somewhere else
        collection.createIndex({ timestamp: 1 }, err => {
          if (err) {
            reject(err);
            return;
          }
        });

        resolve([client, collection]);
      });
    });
  });
}

module.exports = {
  connectToCollection: connectToCollection
};
