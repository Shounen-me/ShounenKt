![alt text](https://i.imgur.com/y0xIsLu.png)

## How to get it 

### Gradle 
```gradle
allprojects {
  repositories {
  	...
  	maven { url 'https://jitpack.io' }
  }
}
```
```gradle
dependencies {
 	implementation 'com.github.manuelkour:ShounenKt:master-SNAPSHOT'
}
```

### Maven
```xml
<repositories>
	<repository>
	  <id>jitpack.io</id>
	  <url>https://jitpack.io</url>
	</repository>
</repositories>
```
```xml
<dependency>
  <groupId>com.github.manuelkour</groupId>
    <artifactId>ShounenKt</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```

## Examples
### Get a user's current profile picture 
```kotlin
val client = ShounenClient("abcxyz") // currently a token is needed to access the API
val profilePicture = client.getUser("Asuha").profilePicture 
// if the picture was not set manually by the user, it will return a random generated one from waifu.pics
```
### Post a new user to the API
```kotlin
val client = ShounenClient("abcxyz") 
client.postUser("166883258200621056", "Asuha")
```

### Sync user's MAL profile to the API
```kotlin
val val client = ShounenClient("abcxyz") 
client.malSync("166883258200621056") 
// --> Returns redirect link to MAL authorization page for the user
```
### Add anime to a user's anime list on MAL 
```kotlin
val client = ShounenClient("abcxyz") 
client.postAnime("166883258200621056", "44074", 11) 
``` 

## To-Do's
- [ ] Add setProfilePicture functionality
- [ ] Add getAnime/getManga functionality
