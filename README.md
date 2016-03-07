#Bike Share Philly Map
Origin: I have an android phone, and use Philly Bike Share to get around town.  Necessity is the mother of invention!
![Bike Share App](readme-assets/bikeshare.png?raw=true)


[![Google Play](readme-assets/google_play.png?raw=true)](https://play.google.com/store/apps/details?id=com.samhalperin.phillybikesharemap)


# Help us test new features in the app, sign up to be an alpha/beta tester.
**This is a big part of helping us release new features to the app while keeping it robust and stable. You also get early access to new features.  You are doing us a big favor by participating, and it's one way you can help support the app!**


+ [Google group for testers.](https://groups.google.com/forum/#!forum/philly-bike-share-map-alphabeta-testers)
+ [Link to sign up to be a tester if you've already got the app.](https://play.google.com/apps/testing/com.samhalperin.phillybikesharemap)

*Please email sam@samhalperin.com if you have any trouble with the above links!*

#Release Notes
###New in version 9 (2.0-alpha1)
New Features:
+ Favorite a kiosk, and view a quick list view of favorites

UI Tweaks:
+ Zoom to "street level" when my current location is clicked
+ Zoom in 1 level on cluster tap (should "explode" clusters and make them more discoverable)
+ Minimal bikeshare-philly icon, cleaner looking UI

New Tech in this release for the geekily oriented:
+ Realm database for favorites
+ Removed reactive code in favor of simpler Retrofit-based REST client
+ Google API client calls for current location
+ Location updates set to best balance of precision and low battery power
+ Better handling of application lifecycle

### New in Version 8 (1.6)
+ Bugfixes and stability enhancements

### Previous versions
Initial release of the app from first hackathon with real time kiosk status, custom map markers, clustering.  
