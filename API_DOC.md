Overview
This API is used to read a CSV with a UTC datetime, latitude and longitude columns and append the timezone the vehicle is in and the localised datetime.

Method
The following REST methods are available:
getTimeZone

Request Header
Accept: text/csv
**keep Contenet-type blank when checking postman

URL
http://localhost:8081/timeZone

Method
POST

Body
'form-data' option should be remain as default.
Choose 'File' option instead of 'text' from dropdown at the right side.

Response
200 OK
406 Not Acceptable
500 Internal Server Error

** output CSV is downloaded to the specified path