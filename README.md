#Installing

Make sure you have following environment variables set:

* `SSO_CLIENT_ID`: The application ID you can find here: [EVE dev site](https://developers.eveonline.com/applications)
* `SSO_CALLBACK_URL`: The callback URL you defined for your application
* ingame rights: Accountant

#TODOS:
* ~~rename collections in mongo~~
* ~~make tax and refining configurable~~
* ~~make worker call order~~
* support for dropping all data except lead char - so we don't have to re-authenticate
* ~~make endpoints to fetch data?~~
* ~~make the workers run at 14 utc for the day before!~~
* ~~trim remove whitespaces in search box~~
* ~~add id for transaction - to easier find the errors~~ not needed anymore
* ~~add corporation to char name in UI~~
* ~~get char id from transaction~~
* ~~keep track of all transactions per month~~
* add a way of combining characters to only transfer one amount
* ~~show delta of ore mined in detail screen~~
* ~~show current refined ore prices~~
* ~~chek mining multiple times a day to prevent API outage~~
* ~~also let char specify a reason when transfering money~~
* ~~reset delta daily~~
* ~~save mining history~~
* ~~add approx. value to actual mining entry~~
* ~~only let people auth once and a way to delete current primary character!~~
* ~~there's a bug that the config is not persisted!~~
* ~~can we have button "copy total debt to clipboard"?~~
* ~~add button for copy the nickname~~

 

 