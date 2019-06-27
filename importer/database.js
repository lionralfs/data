const { MongoClient } = require('mongodb');

const cache = {};

async function connectToCollection(collection) {
  if (cache[collection] !== undefined) {
    return cache[collection];
  }

  const client = new MongoClient('mongodb://localhost:27017', { useNewUrlParser: true });
  await client.connect();

  const db = client.db('airdata');
  const col = await db.collection(collection);

  cache[collection] = [client, col];

  return [client, col];
}

module.exports = {
  connectToCollection: connectToCollection
};
