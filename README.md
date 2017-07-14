# Bike Share Philly Map
![Bike Share App](readme-assets/bikeshare.png?raw=true)


[![Google Play](readme-assets/google_play.png?raw=true)](https://play.google.com/store/apps/details?id=com.samhalperin.phillybikesharemap)


#Release Notes

###New in version 11 (2.0-alpha3) 
+ Fail faster on no-network-connection

###New in version 10 (2.0-alpha2)
+ Bug fixes for my location

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
