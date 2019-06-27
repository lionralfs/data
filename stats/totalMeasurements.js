db.getCollection('sensordata').aggregate([{ $group: { _id: null, measurementCount: { $sum: { $size: '$measurements' } } } }]);
