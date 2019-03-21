# HOW TO HIDE API KEY USING GRADLE

The API key is put inside gradle.properties file. We do this so the api key wont be seen in our git commits, and whenever we sync the project it wont be there.

	

## Define Key In build.gradle	
		
Open gradle.properties file in window path below

```
Windows: C:\Users\your_user_name\.gradle\gradle.properties	
Mac: /Users/<Your Username>/.gradle
Linux: /home/<Your Username>/.gradle
```
		

## Append Text		
Use notepad++ or sublime or any text editor to open gradle.properties file and append code below
add your code inside

```
MyMovieDbApiKey="your_api_key"
```
		
		
## Open Android Studio		
 Find the build.gradle(Module.app) file under app directory of your android studio project and put the code below 
		
```
buildTypes {
release {
	minifyEnabled false
	proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
}
}
//add code below
buildTypes.each{
it.buildConfigField 'String', 'MY_MOVIE_DB_API_KEY', MyMovieDbApiKey
}
```
		
**Make sure you sync your project else you wont see this key in code**
		
		
		
		
## Getting Key In Code 

 ```	
 import your.package.appname.BuildConfig;
 String key = BuildConfig.MY_MOVIE_DB_API_KEY; 
 ```
