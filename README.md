[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-AppInfoBadge-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/8066)

# AppInfoBadge

A simple about screen library that can display the following information:
 - App permissions list
 - License page
 - Libraries page
 - Changelog page
 - PlayStore rate link
 - Contact page

![](demo.gif)


## Download
```
allprojects {
 repositories {
  jcenter() 
  maven { url "https://jitpack.io" }
 } 
}
```
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
  .withLicense { true }  
  .withLibraries { true }
  .withRater { true }  
  .withEmail { "developer@gmail.com" }  
  .withSite { "https://www.developer.com" }
  .show()
```

#### Changelog
An HTML file named **changelog.html** that you have to put under ***assets***.
##### Sample:
```
<html>
    <body>

        <h2>Version 3</h2>
        <p>This new version is amazing</p>

        <h2>Version 2</h2>
        <p>Bugfixes and general improvements</p>

        <h2>Version 1</h2>
        <p>Initial release</p>
    
    </body>
</html>
```
#### Libraries
An HTML file named **libraries.html** that you have to put under ***assets***.
##### Sample:
```
<html>
    <body>
        <ul>
            <li><a href="https://github.com/ditacristianionut/AppInfoBadge">AppInfoBadge</a></li>
            <li><a href="https://github.com/afollestad/material-dialogs">Material Dialogs</a></li>
        </ul>
    </body>
</html>
```
#### License
An HTML file named **license.html** that you have to put under ***assets***.
##### Sample:
```
<html>
    <body>

        <h2>Apache License, Version 2.0</h2>
        <h3>
            Apache License
            Version 2.0, January 2004
            http://www.apache.org/licenses/
        </h3>
        <p>
            Licensed under the Apache License, Version 2.0 (the "License");
            you may not use this file except in compliance with the License.
            You may obtain a copy of the License at
        
            http://www.apache.org/licenses/LICENSE-2.0
        
            Unless required by applicable law or agreed to in writing, software
            distributed under the License is distributed on an "AS IS" BASIS,
            WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
            See the License for the specific language governing permissions and
            limitations under the License.
        </p>

        <h2>GNU General Public License version 3</h2>
        <h3>
            License Copyright: Copyright © 2007 Free Software Foundation, Inc.
        </h3>
        <p>
            This program is free software: you can redistribute it and/or modify
            it under the terms of the GNU General Public License as published by
            the Free Software Foundation, either version 3 of the License, or
            (at your option) any later version.
        
            This program is distributed in the hope that it will be useful,
            but WITHOUT ANY WARRANTY; without even the implied warranty of
            MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
            GNU General Public License for more details.
        
            You should have received a copy of the GNU General Public License
            along with this program.
        </p>

        <h2>MIT License</h2>
        <p>
            Copyright (c) <2020> <>
            Permission is hereby granted, free of charge, to any person obtaining a copy
            of this software and associated documentation files (the "Software"), to deal
            in the Software without restriction, including without limitation the rights
            to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
            copies of the Software, and to permit persons to whom the Software is
            furnished to do so, subject to the following conditions:
        
            The above copyright notice and this permission notice shall be included in all
            copies or substantial portions of the Software.
        
            THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
            IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
            FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
            AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
            LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
            OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
            SOFTWARE.
        </p>

    </body>
</html>
```
