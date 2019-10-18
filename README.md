# Data Backend / API

## Installation / Setup

You need [docker-compose](https://docs.docker.com/compose/install/) installed to run the MongoDB database. To start it, run `docker-compose up` in the project root directory. This should start the MongoDB docker container.

Take a look at `src/main/resources/application.dummy.yml` and copy the contents to a new file `src/main/resources/application.yml`. Here you can set some application parameters (mongoDB connection parameters should be correct for local development).
The `apiKey` parameter is relevant so API endpoints that allow uploads (predictions and heatmaps) are protected. Pick a random string. The same string has to be used as an environment variable when running the ML/interpolation scripts.

To compile and run the Java backend, run:

```sh
mvn clean package && java -jar target/AirDataBackendService.war
```

However, this only boots up the API, without a frontend. To include the frontend, stop the application from running and follow these steps:

1. Check out the [frontend repository](https://github.com/base-camp-luftdaten/frontend).
2. Follow the "[Build](https://github.com/base-camp-luftdaten/frontend/blob/master/README.md#build)" instructions.
3. In this repository, delete everything in `src/main/resources/static`. If the `static` directory doesn't exist, create it.
4. In the frontend repository, copy everything in `dist`.
5. Paste it into `src/main/resources/static`.
6. Run `mvn clean package && java -jar target/AirDataBackendService.war` again to start the application.
7. Open [http://localhost:8080](http://localhost:8080).

Steps 3, 4 and 5 can be done easily by using `rm -rf ../data/src/main/resources/static && cp -a dist/. ../data/src/main/resources/static`, assuming you are currently in the frontend-repository and your backend-repository is under `data`.

## Manual data import

While the application imports new data on its own (in a 4 minute interval) it might be useful to trigger a manual import. This is currently the only way to import historical (dust) data.

To achieve this, you need [Node.js](https://nodejs.org/en/) version 8+. First, run `npm install` in the project root. You can run the import script by using `node importer/24h.js --single-day`. This should feed yesterdays dataset into the database.

The `--single-day` flag indicates that only a single day should be imported.

It is also possible to specify the day that should be imported by passing the date in the following format: `node importer/24h.js 2019-08-15 --single-day`. The default value is yesterday.

When run without using `--single-day`, the importer will import the entire dataset (day-by-day), starting with the passed date (or yesterday, if no date was specified).

> Note: Ommitting `--single-day` could take a long time.

To inspect the MongoDB database using a GUI, I recommend [Robo 3T](https://robomongo.org/).

## Deployment

TODO: add description on how to update app on the server

1. ```sh
   rsync -avz importer/ username@server:/srv/data/luftdaten19/importer
   rsync -avz package.json username@server:/srv/data/luftdaten19/
   ```
2. Login on the server via `ssh`, change into the `/srv/data/luftdaten19` directory and run `npm install`

## Skipping tests when compiling

This can be useful if you need to compile without having the entire project set up, since the tests require a running mongoDB instance. Add the following CLI flag to skip the unit tests:

```sh
-Dmaven.test.skip=true
```
