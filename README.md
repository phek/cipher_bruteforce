# Cipher Brute Force
Program for brute forcing Vigenere Ciphered Base64 strings.

The server hands out key-ranges for the clients to crack, the clients then report their results back to the server. The more clients you run the faster the brute force will complete. If you want to generate your own Vigenere ciphered base64 string you can do this by first encoding the string with base64 at [https://cryptii.com/](https://cryptii.com/), then cipher the base64 encoded string at [https://www.dcode.fr/vigenere-cipher](https://www.dcode.fr/vigenere-cipher), cipher it by entering the string in the encode field, choose a key of your choice, the longer the key is, the longer the brute force will take.

## Client Installation
Extract the build folder from the Java client (java_client/build) to a location of your choice.

### Run Client (Requires Java)
Navigate to the _java_client/build/_ folder.  
Open run.bat on windows.  
Run the following Java command on other platforms: _java -jar java_client.jar_

### Commands
**current** - Displays the current key that is being handled by any of the clients  
**results** - Displays all found results (inclusive false positives)  
**exit** - Exits the program after finishing it's current work (Always use this to prevent missing results)  

## Server Installation
Extract the server folder to a location of your choice.

### Run Server (Requires NPM)
Navigate to the _server_ folder.  
Open run.bat on windows.  
Run the following command on other platforms: _npm start_

### Commands
**text** _Text (Base64VigenereString)_ - Changes the current text that should be cracked by the clients.  
**restart** - Restarts the progress, restart from zero.  
**padding** _Padding (Number)_ - Changes the key-range oadding. The amount of keys each client should crack at a time.  
**current** - Displays the currently handled key.  
**performance** - Shows the current average crack speed.
**exit** - Exits the program.
