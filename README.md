# Инструкция по запуску


С использованием Docker:
docker build . -t abcase
docker run -p 8080:8080 -t abcase

С использованием Gradle:
gradlew bootRun

После старта приложения доступно два эндпоинта:

http://localhost:8080/api/v0/rates/ отображает доступные трёхбуквенные коды валют

http://localhost:8080/api/v0/rates/{currenciesCode} возвращает гиф в соответствии с заданием.

Вместо {currenciesCode} указывается код валюты, например RUB. 

Настройки базовой валюты и другие параметры можно поменять в application.properties файле.
