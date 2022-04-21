
# Telegram bot Vocabulary-week3.1

The application implements the simple telegram bot "Short Latin-English vocabulary"

1) Install Docker
2) Clone the repository
3) Go to the project folder, vocabulary-bot-week3.1
4) Create a docker image:
     windows
       docker build -t vocabulary-bot-10-1 .
     Linux
       sudo docker build -t vocabulary-bot-10-1 .
5) Run
     Windows
      docker run --name vocabulary-bot-10-1 -d --env-file=src/main/resources/dev.env vocabulary-bot-10-1
     Linux   
      sudo docker run --name vocabulary-bot-10-1 -d --env-file=src/main/resources/dev.env vocabulary-bot-10-1