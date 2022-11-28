#### get All Meals
'curl http://localhost:8080/topjava/rest/meals'

#### get Meals Filtered
curl "http://localhost:8080/topjava/rest/meals/filtered?startDate=2020-01-30&startTime=20:00&endDate=2020-01-31&endTime=22:00"

#### get Meal 100003
curl http://localhost:8080/topjava/rest/meals/100003

#### create New Meal
curl -X POST "http://localhost:8080/topjava/rest/meals" -H "Content-Type: application/json" -d "{\"dateTime\":\"2020-02-01T18:00\",\"description\":\"Created meal\",\"calories\":300}"

#### update Meal 100003
curl -X PUT "http://localhost:8080/topjava/rest/meals/100003" -H "Content-Type: application/json" -d "{\"dateTime\":\"2020-01-30T10:02\",\"description\":\"Updated meal\",\"calories\":200}"

#### delete Meal 100003
curl -X DELETE http://localhost:8080/topjava/rest/meals/100003