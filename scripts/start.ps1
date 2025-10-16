Param(
  [string]$Profile="dev"
)
Write-Host "Starting Spring Boot (profile=$Profile)"
./mvnw spring-boot:run -Dspring-boot.run.profiles=$Profile

