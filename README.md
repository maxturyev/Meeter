Для проверки
```cmd 
curl -v localhost:9090/events
```
```cmd 
curl --location --request GET 'http://localhost:9090/events' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Add your name in the body",
    "category": "Add your name in the body"
}'
```
```cmd 
curl --location --request PUT 'http://localhost:9090/events/2' \
--header 'Content-Type: application/json' \
--data '{
	"name": "",
	"category": ""
}'
```
```cmd 
curl --location --request DELETE 'http://localhost:9090/events/2' \
--data ''
```
