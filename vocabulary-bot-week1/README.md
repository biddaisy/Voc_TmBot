# Telegram bot Vocabulary-week1

An application implements a simple telegram bot.

1) Install Jdk 11.0.4 or higher
2) Clone the repository
3) Go to the project folder, vocabulary-bot-week1
4) Run 
 mvnw clean package
5) Go to the folder target
6) Run
 Note. You should provide the following environment variables
  bot_username=<yourbot_bot> 
  bot_token=<yourbot_token>
 
 Command line: 
  java -jar vocabulary-bot-1.0.0.jar --bot_username=<yourbot_bot> --bot_token=<yourbot_token>
   or
  mvnw clean compile exec:java -D=bot_username=<yourbot_bot> -Dbot_token=<yourbot_token>