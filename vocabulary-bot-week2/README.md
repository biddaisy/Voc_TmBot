# Telegram bot Vocabulary-week2

An application implements a simple telegram bot.

1) Install Jdk 11.0.4 or higher
2) Install Docker
3) Clone the repository
4) Go to the dumps folder in the project folder, vocabulary-bot-week2
5) Run
	docker-compose --project-name mongodb up -d
6) Go to the project folder, vocabulary-bot-week2
7) Run 
	mvnw clean package
8) Go to the folder target
9) Run
 Note. You should provide the following environment variables
  bot_username=<yourbot_username> 
  bot_token=<yourbot_token>
 
 Command line: 
  java -jar vocabulary-bot-1.0.0.jar --bot_username=<yourbot_username> --bot_token=<yourbot_token>
  
  or return to the project folder, vocabulary-bot-week2 and run

  mvnw clean compile exec:java -D=bot_username=<yourbot_username> -Dbot_token=<yourbot_token>