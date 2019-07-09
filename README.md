# EROAD_Test

LAT LONG to Timezone and UTC Date to Local date Converter
=========

Develop a small application to read a CSV with a UTC datetime, latitude and longitude columns and append the timezone the vehicle is in and the localised datetime. 

See example of CSV input and output below. We will then run this over several test files with several rows of data. 

**Input** 
2013-07-10 02:52:49,-44.490947,171.220966 

**Output** 
2013-07-10 02:52:49,-44.490947,171.220966,Pacific/Auckland,2013-07-10 14:52:49

---

##Solution
 
-> Developed a REST api to accept input as a CSV file
-> Read the content of the CSV file and for each row get the latlng position to find the timezone using Google APIs
-> Converts UTC Time into Local date time
-> Construct the output CSV with the updated data and download the output csv in the specified location

##Testing

->Junit testing have been implemented to test the convert() functionality

## Sample input and output CSV are attached

Swagger documentation URL : http://localhost:8081/swagger-ui.html#/time-zone-tracker-controller/getTimeZoneUsingPOST

NOTE : Please change the file download path in boostrap (file.upload.dir).properties to an accessible location
	   The given application start port is 8081. If required can change the server.port property in boostrap.properties