# Cipher Brute Force
Program for brute forcing Vigenere Ciphered Base64 strings.

If you want to generate your own Vigenere ciphered base64 text you can do this by first encoding a text to base64 at [https://cryptii.com/](https://cryptii.com/) and then ciphering the base64 encoded string at [https://www.dcode.fr/vigenere-cipher](https://www.dcode.fr/vigenere-cipher), cipher it by entering the string in the encode field, choose a key of your choice, the longer the key is, the longer the brute force will take. Use the **text** command on the server to change to the new text.

Example:  
[Encode base64](https://i.gyazo.com/37584b7e7c10b480a01be018226604e5.png)  
[Cipher with vigenere](https://i.gyazo.com/c13e2ff49abd4a358ba7e36d633cea13.png)  
[Change the text to decode on the server](https://i.gyazo.com/cb230be2330989f2c1cdfb017efbafd7.png)

A 6 character key should be found within 5 minutes depending on computer speed and amount of clients. A 7 character key can take days. The default message has a 6 character key, try running the program to see the brute force in progress, run multiple clients to increase the speed.

## Client Installation (Requires Java)
Extract the build folder from the Java client (java_client/build) to a location of your choice.

### Run Client
Navigate to the _java_client/build/_ folder.  
Open run.bat on windows.  
Run the following Java command on other platforms: _java -jar java_client.jar_

### Commands
**connect [server ip] [server port]** - Connects to the server, ip and port optional, default is localhost:5000.   
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
**text [text]** - Changes the current text that should be cracked by the clients, text is a Base64VigenereString and is mandatory.  
**restart** - Restarts the progress, restart from zero.  
**padding [padding]**- Determines how many keys each client should handle at a time, padding is a Number and is mandatory.  
**current** - Displays the currently handled key.  
**performance** - Shows the current average crack speed.
**exit** - Exits the program.
