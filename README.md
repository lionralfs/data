# Data import

## Installation / Setup

```sh
mvn package && java -jar target/AirDataBackendService-0.0.1-SNAPSHOT.jar
```

## REST Endpoints

### GET `/api/measurements/dust{query}`

Returns all _dust only_ measurements.

Request headers:

```
Accept: application/json
```

Query parameters:

- maxage: only return measurements up to this timestamp, _default_: -1 day
  - Format: `YYYYMMDDThhmmssZ`
  - Example: `/api/measurements/dust?maxage=20190418T133000Z`
- box: only include sensors in a 'box' with the given coordinates
  - Format: 4 comma separated values: `lat1,lon1,lat2,lon2`, `lat1/lon1` represent the bottom left point, `lat2/lon2` the top right point
  - Example: `/api/measurements/dust?box=48.7820,9.1920,51.0440,13.7460`
- country: only return measurements within the specified countries
  - Format: ISO 3166-1 Alpha-2 country codes, comma separated
  - Example: `/api/measurements/dust?country=DE`
  - Example: `/api/measurements/dust?country=DE,NL`

Result:

```json
[
  {
    "id": 3395327500,
    "timestamp": "2019-04-18 11:27:20",
    "location": {
      "id": 11856,
      "latitude": "51.0440",
      "longitude": "13.7460",
      "altitude": "115.3",
      "country": "DE"
    },
    "sensor": {
      "id": 36,
      "sensor_type": {
        "id": 14,
        "name": "SDS011",
        "manufacturer": "Nova Fitness"
      }
    },
    "sensordatavalues": [
      {
        "id": 7202606783,
        "value": "0.30",
        "value_type": "P1"
      },
      {
        "id": 7202606786,
        "value": "0.30",
        "value_type": "P2"
      }
    ]
  },
  {
    ...
  }
]
```

### GET `/api/measurements/humidity{query}`

Returns all _humidity only_ measurements.

Request headers:

```
Accept: application/json
```

Query parameters: same as `/api/measurements/dust`.

Result: same as `/api/measurements/dust`, except it returns only humidity measurements.
