# Telegram bot Vocabulary-week3

The application implements the simple telegram bot "Short Latin-English vocabulary"

1) Install Jdk 11.0.4 or higher
2) The mongo db resides on wandering-sun-vs.icdc.io:27017
3) Clone the repository
4) Go to the dumps folder in the project folder, vocabulary-bot-week3
5) Import the words by running 
    import.words.cmd
   (change the username, password and database accordingly to your settings)
6) Go to the project folder, vocabulary-bot-week3
7) Run 
    mvnw clean package
8) Go to the folder target
9) Run
 Note. You should provide the following environment variables
   bot.name=<your_bot_name> 
   bot.access-token=<your_bot_token>
   spring.data.mongodb.database=<your_DB> 
   spring.data.mongodb.username=<your_user>
   spring.data.mongodb.password=<your_password>
  
 Command line:
  java -jar vocabulary-bot-1.0.0.jar --bot.name=<your_bot_name> --bot.access-token=<your_bot_token> --spring.data.mongodb.database=<your_DB> --spring.data.mongodb.username=<your_user> --spring.data.mongodb.password=<your_password>
  
  or return to the project folder, vocabulary-bot-week3 and run

  mvnw clean compile exec:java -Dbot.name=<your_bot_name> -Dbot.access-token=<your_bot_token> -Dspring.data.mongodb.database=<your_DB> -Dspring.data.mongodb.username=<your_user> -Dspring.data.mongodb.password=<your_password>