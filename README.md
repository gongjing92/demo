# Mini Salary Program

A simple Java Spring Boot web application that can pull user's name and salary details from MongoDB. The application also allows users to upload data to MongoDB through a CSV file.

## Description

The application will preload the database with sample data on start up if there is no data in the database. 

This web application contains 2 endpoints. 

`GET /users`

Returns all users' details in JSON format. Has 5 request params to filter the results - min (defaults to 0) , max (defaults to 4000), offset (defaults to 0), limit (defaults to no limit) and sort (name/salary).

Any mismatch in input type will result in 403 BAD REQUEST.

`POST /upload`

Allows users to upload a CSV file to update the documents in MongoDB. File upload is an all-or-nothing operation. Any error in the CSV file will result in the whole file not being updated into the database.

Any file types other than CSV will result in 403 BAD REQUEST.

### Example CSV file

`valid.csv` - contains rows with valid input. Includes 0, negative number.

`invalid.csv` - contains rows with invalid input.
