# Data import

## REST Endpoints

### GET `/api/measurements/dust{query}`

Returns all _dust only_ measurements.

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

### GET `/api/measurements/humidity{query}`

Returns all _humidity only_ measurements.

Query parameters: same as `/api/measurements/dust`.
