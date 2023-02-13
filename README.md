## How to build the project

In order to build the app you will either:
- need to remove all Firebase dependencies 
- add Firebase project files

The **google-services.json** needs to be put into **app/**

Also for firebase app distribution a new service account is needed 
(Firebase Project settings -> Service accounts -> Generate new private key)

This private key needs to be renamed to **serviceAccountKey.json** and moved to **app/**