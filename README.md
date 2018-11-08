# Vehicle Tracker

## Assumptions

The assumptions are mentioned in code comments

## Notes

1. It was better for dependencies (TimeUtil & FileReader) to be injected through constructor to make the class testable
2. I wrote only sample test cases, a lot of tests are missing to save time

## Endpoints

Get localhost:8081/tracker/info/vehicle?time={time_string}&x={x_coordinate}&{y_coordinate}

Response 
        {
          "vehicleId": 3,
          "lineId": 0,
          "lineName": "M4"
        }


Get localhost:8081/tracker/info/vehicle?stopId={stop_id}

Response 
        {
          "vehicleId": 3,
          "lineId": 0,
          "lineName": "M4"
        }

Get localhost:8081/tracker/info/line/{line_id}/delayed

Response 
        true
        
## Build & Run

sbt build
sbt run

