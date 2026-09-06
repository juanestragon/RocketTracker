# Rocket Tracker

Rocket Tracker is a free open-source program that provides analytics using the [Rocket League Stats API](https://www.rocketleague.com/developer/stats-api "Stats API").

Go to [Setup](https://github.com/juanestragon/RocketTracker/edit/main/README.md#Setup) to learn how to use it.

![alt text](https://github.com/juanestragon/RocketTracker/blob/main/screenshots/HomeView.png "Home View")
![alt text](https://github.com/juanestragon/RocketTracker/blob/main/screenshots/StatisticsView.png "Statistics View")
![alt text](https://github.com/juanestragon/RocketTracker/blob/main/screenshots/SettingsView.png "Settings View")



## Setup

To use it you first have to enable the feature, in order to do that go to your game installation folder and search for /TAGame/Config/DefaultStatsAPI.ini,
you should see something like that:
```
[TAGame.MatchStatsExporter_TA]

; Port the client will listen for tcp connections on (must be different than WebPort, set to 0 to disable)
Port=49123

; Port the client will listen for web connections on (must be different than Port, set to 0 to disable)
WebPort=49124

; How many times per second the game sends the update state (capped at 120, 0 disables this feature)
PacketSendRate=0
```

Change the PacketSendRate to a valid value between 1-120, I recommend 10, and make sure you put the same value in the app's setting so it measures the data correctly.

## More

This tracker only tracks competitive matches, and does not get MMR statistics, if some time epic adds a public API to track MMR I will implement it, I have asked for it in the epic games support service and you can ask for it too [here](https://www.epicgames.com/help/rocket-league-c-37599050/gameplay-c-32343914/cuales-son-los-sitios-y-las-cuentas-oficiales-de-redes-sociales-de-rocket-league-a22635832). 
