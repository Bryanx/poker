# Description
Online multiplayer poker application, can be played in browser and on an Android device.

Made in Spring and Angular.

# Screenshots
The main poker table:

![poker1](https://github.com/Bryanx/poker/blob/master/web-client/src/assets/img/poker1.png)

The main menu:

![poker2](https://github.com/Bryanx/poker/blob/master/web-client/src/assets/img/poker2.png)

Notifications:

![poker3](https://github.com/Bryanx/poker/blob/master/web-client/src/assets/img/poker3.png)

# Notice
This was a school project and at the moment I would only use this as an inspiration if you want to create a Poker app. The core mechanics of Poker are fine and the services are internally connected but were never really fully fleshed out and optimized.

If you really want to deploy it, here is how we did it:
- The game-service and user-service are Spring Boot projects that were deployed on Heroku Postgress DB.
- The web-client is an Angular 7 project that was also deployed on Heroku.
- We didn't release the Android app to the Play Store. But you can run it locally and connect it to service layer.

Some known issues:
- Because a Spring Boot project takes quite some time to build and deploy it's possible Heroku thinks the app is timed out before it can startup.
- Because a Spring Boot projects takes up a lot of resources it's possible the limit is achieved very fastly on Heroku free version (i think it was about 2GB at the time).
- In hindsight, if you want to build a small-scale poker app, it's probably a better idea to build the core services in NodeJS or Ktor as they require much less resources to deploy.
