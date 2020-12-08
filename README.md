# autoclick_userinfochecker

Auto click to get user info


## Setup project

- Import dk_lib

    ```bash
    cd src/
    ln -s /Users/compet/workspace/darkcompet/libraries/java/dk_java/src/tool/compet/core
    ln -s /Users/compet/workspace/darkcompet/libraries/java/dk_java/src/tool/compet/automation
    ```

- Copy all libraries to `lib/`

- At IntelliJ do below
    - Choose File -> Project Structure -> Libraries
    - Click + to add libs
    - Choose Java -> Add all libs under `lib/`

- Download jdk and move it to `jdk` folder.

> https://drive.google.com/drive/folders/1tNMd8imMqhpSqVP8n1_osGHSDz42NOPO?usp=sharing

- Download target selenium driver and move to `asset/driver`

    ```bash
    # Chrome
    https://chromedriver.chromium.org/downloads
    
    # Firefox
    https://github.com/mozilla/geckodriver/releases
    
    # IE
    https://www.selenium.dev/downloads/
    ```

- Execute runner

    ```bash
    # Mac
    sh runner_mac.sh
    
    # Win
    Click file `runner_win.bat`
    ```

