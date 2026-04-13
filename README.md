## Java Spring Boot application. (not finished)
### Educational project. Takes data from [Open F1](https://openf1.org/docs/), saves in db, has its own REST API. Used for my other project F1_react.
### Endpoints:
#### Sync:
##### Base /api/sync
- /{year} - syncs full data for whole season
#### Session:
##### Base /api/sessions
- /detailed/{session_key} - returns detailed data for session (session + meeting data)
#### Meeting:
##### Base /api/meeting
- /{meeting_key} - returns data for meeting
- /year/{year} - returns meetings of year/season
#### Lap:
##### Base /api/laps
- /session_key={sessionKey}&driver_number={driverNumber} - returns driver laps of session
- /detailed/session_key={sessionKey}&driver_number={driverNumber} - returns driver laps of session (detailed - with more driver info)
- /detailedForAllDrivers/session_key={sessionKey} - returns detailed data for all drivers of session
#### Driver:
##### Base /api/drivers
- /{session_key} - returns drivers for session


