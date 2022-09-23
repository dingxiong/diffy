# push image
docker buildx build --platform linux/amd64 --push -t dingxiong/diffy:latest .

# build
in mac M1
mvn package -f pom-mac-arm.xml

# run example services

javac -d example src/test/scala/ai/diffy/examples/http/ExampleServers.java

java -cp example ai.diffy.examples.http.ExampleServers 9000 9100 9200

# run diffy
java -jar ./target/diffy.jar \
--candidate='localhost:9200' \
--master.primary='localhost:9000' \
--master.secondary='localhost:9100' \
--responseMode='candidate' \
--service.protocol='http' \
--serviceName='ExampleService' \
--proxy.port=8880 \
--http.port=8888 \
--logging.level.ai.diffy.proxy.ReactorHttpDifferenceProxy=DEBUG

# verify
go to localhost:8888