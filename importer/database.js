const { MongoClient } = require('mongodb');

/**
 * @return {Promise<[MongoClient, import('mongodb').Collection<any>]>}
 */
async function connectToCollection(collection) {
  const client = new MongoClient('mongodb://localhost:27017', { useNewUrlParser: true });
  await client.connect();

  const db = client.db('airdata');
  const col = await db.collection(collection);

  return [client, col];
}

module.exports = {
  connectToCollection: connectToCollection
};
