# strongbox
Java based graphical application for simple document encryption

## Overview of assignment
  I set out to build an encryption application for desktop using JavaFX and Scenebuilder with NetBeans IDE. The application utilises Java.Crypto and Java.security to generate passwords and access ciphers. The project has been challenging given I had little knowledge of cryptography prior to starting so it has been both a Java Graphics and Crypto assignment!

## Purpose
  Simple file encryption software that gives the user granular control over type of file encryption.

  To prevent insecure passwords the application generates the seed randomly
  The encryption key (following encryption) is returned to the user and is available for download once. After reset or download the session is cleared and the key cannot be recovered.

  The software is designed to make available the maximum cipher complexity (RSA 1068), however, due to JRE versions most machines have a max of 128. The software will utilise the strongest algorithms.

## Features

* Choice of encryption methods depening on local architecture (AES, DES, RSA, DSAese)
* Random password generator to match method level
* Session based to ensure machine can be cleaned
* Keylength based on encryption method Choice
* Can reach RSA 1068 file encryption standard (if JRE supports)
* Unit testing built in on applicaiton launch to validate ciphers and test sessions


#Implementation stages

## 1. Backend development

  Started working on command line encryption tool. Used the standard Java.crypto API so got working fairly quickly. Found a number of useful resources online (including Oracle library) which are cited in the code where i've used templates.

## 2.  Basic graphical interface

  Attempted to deploy a basic UI using the NetBeans & Scenebuilder templates. Encountered issues with selecting files and settled on FileChooser as the best method for finding targets and sources:

  ```
  FileChooser fileChooser = new FileChooser();
        //  Launches a dialogue window to the main scene
        selectedFile = fileChooser.showOpenDialog(null);
        //  Handle cases where the user aborts or the process fails
  ```

  This is tested worked. Added null handling and appropriate Alert dialogue to the user for incorrect entry.

## 3. Testing
  Added automated security testing to the program which calls a hard stop (exit) if any of the conditions are not met. I decided to build this automated testing into the launch routine of the GIU as a feature of the platform. Given that this perform some integrity checks. e.g. validate algorithms that are available through the API (they vary depending on JRE version) and that the sessions are resetting properly.

  The testing exposed that the max key length seems to vary across JRE's depending on the legal framework in the country of use. i therefore added a warning to the interface should the program detect the keylength is capped (meaning the cipher could potentially be broken with advanced tools/time).

  Needless to say, I haven't exceeded 128 bit encryption as I'm unsure of the legal issues so the current application caps at 128 by default.


## 4. Basic frontend functionality
  Starting working to implement genuine user interfaces to allow for the selection of an encryption cipher from a drop down, adding in the password if needed for decryption and handling file upload. Used @FXML tags extensively to do this, buttons, labels etc.

  The choice box took some adjustment to make sure it worked correctly. Added in a 'First choice default option' to escape issues where users don't specify an option but click 'Encrypt' with a file present, for example.

  Built a branch that had multiple scenes to handle popups and alerts (javafx.Popup); Got in a mess with multiple scenes

## 5.  States and sessions
  Testing showed that each time a cipher was created and used it could be reused. Also needed a simple way to 'clear' the application for re-use between tasks but didn't want to overcomplicate the UI. Introduced 'State' class : with the intention that a new 'State' is created for each 'Session'. A session is a task being completed and can be reset back to default. The session contains the data needed to encrypt and is the only place that holds the 'key':
    ```
       private Boolean ready = false;
       private Boolean encryption = true;
       private int cipherMode = ENCRYPT_MODE;
       private String method;
       private String direction = "Encryption";
       private String key = null;
       private int keylength = 0;
       private File target;
       private File input;
      ```
  Once all the conditions for an encryption or a decryption are met the state changes to 'Ready' which then permits a cipher to be created. Once a key is downloaded the state is reset and the cipher deleted. This prevents users from extracting the ciphers or the keys from the application after the encryption.

## 4. Exception handling
  Added in handling for user error e.g. trying to encrypt an empty file or handling errors writing to file/cipher. These generally throw the user a security alert or an information alert and there are helper functions for these:
  ```
  private void showSecuritywarning(String message){
    Alert alert = new Alert(AlertType.WARNING);
    alert.setTitle("Strongbox");
    alert.setHeaderText("Security Alert");
    alert.setContentText(message);
    alert.show();

    }
  ```

## 5. Download and save key
  Added the ability to download the encryption key (generated) once it is complete. Used File selection dialogue to choose location as previously.


## 6. Layout and appearance
  Changed the aspect of the application and added a 'Strongbox' logo. Added an 'Information' text object at the footer of the window to help users navigate.


## 7. Password generator
  Added functionality to create passwords automatically when encrypting. Utilised cryptographically strong random number generator to achieve this. (Java Secure random) to generate secret phrases that are of the correct length based on the desired encryption algorithm. This was a key objective of the software: essentially it ensures that the encryption is always of the very highest level in terms of key (seed) and therefore cipher.
  Introduced a new class 'Randompass' which once initialised returns a Hex string of the desired length. This uses the Math/long random function to generate as well as Java.Security.
