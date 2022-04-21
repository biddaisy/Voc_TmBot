FROM mongo:focal

COPY words.json /words.json
CMD mongoimport --drop --jsonArray --host mongodb --port 27017 -d vocabulary_DB  --file words.json --collection words 

