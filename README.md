

# AppInfoBadge

A simple about screen library that can display the following information:
 - App permissions list
 - Licenses page
 - Changelog page
 - PlayStore rate link
 - Contact page

![](demo.gif)


## How to integrate
- Add the jitpack.io repository:
```
allprojects {
 repositories {
  jcenter() 
  maven { url "https://jitpack.io" }
 } 
}
```
- then
```
dependencies { 
  implementation 'com.github.ditacristianionut:AppInfoBadge:$version'
}
```

## How to use
Use AppInfoBadge to create an instance of AppInfoBadgeFragment configured as you wish:
```
 val aboutPage = AppInfoBadge  
  .darkMode { true }  
  .headerColor { Color.RED }  
  .withAppIcon { true }  
  .withPermissions { true }  
  .withChangelog { true }  
  .withLicenses { true }  
  .withRater { true }  
  .withEmail { "developer@gmail.com" }  
  .withSite { "https://www.developer.com" }
  .show()
```

#### Changelog
An HTML file named **changelog.html** that you have to put under ***assets***.
#### Licenses
An HTML file named **licenses.html** that you have to put under ***assets***.
