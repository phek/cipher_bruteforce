# Cipher Brute Force
Program for brute forcing Vigenere Ciphered Base64 strings.

The server hands out key-ranges for the clients to crack, the clients then report their results back to the server. The more clients you run the faster the brute force will complete. If you want to generate your own Vigenere ciphered base64 string you can do this by first encoding the string with base64 at [https://cryptii.com/](https://cryptii.com/), then cipher the base64 encoded string at [https://www.dcode.fr/vigenere-cipher](https://www.dcode.fr/vigenere-cipher), cipher it by entering the string in the encode field, choose a key of your choice, the longer the key is, the longer the brute force will take.

## Client Installation (Requires Java)
Extract the build folder from the Java client (java_client/build) to a location of your choice.

### Run Client
Navigate to the _java_client/build/_ folder.  
Open run.bat on windows.  
Run the following Java command on other platforms: _java -jar java_client.jar_

### Commands
**connect** **[server ip]** *(IP-address - Optional - Default: localhost)* **[server port]** *(Number - Optional - Default: 5000)* - Connects to the server   
**current** - Displays the current key that is being handled by any of the clients  
**results** - Displays all found results (inclusive false positives)  
**exit** - Exits the program after finishing it's current work (Always use this to prevent missing results)  

## Server Installation (Requires NPM)
Extract the server folder to a location of your choice, then run install.bat on windows or _npm install_ in a command line on other platforms.

### Run Server
Navigate to the _server_ folder.  
Open run.bat on windows.  
Run the following command on other platforms: _npm start_

### Commands
**text** **[text]** *(Base64VigenereString - Mandatory)* - Changes the current text that should be cracked by the clients.  
**restart** - Restarts the progress, restart from zero.  
**padding** **[padding]** *(Number - Mandatory)* - Changes the key-range oadding. The amount of keys each client should crack at a time.  
**current** - Displays the currently handled key.  
**performance** - Shows the current average crack speed.
**exit** - Exits the program.
