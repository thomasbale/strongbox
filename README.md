# strongbox
Java based graphical application for simple document encryption

Overview of assignment
  I set out to build an encryption application for desktop using JavaFX and Scenebuilder with NetBeans IDE. The application utilises Java.Crypto and Java.security to generate passwords and access ciphers. The project has been challenging given I had little knowledge of cryptography prior to starting so it has been both a Java Graphics and Crypto assignment!

Purpose
  Simple file encryption software that gives the user granular control over type of file encryption.

  To prevent insecure passwords the application generates the seed randomly
  The encryption key (following encryption) is returned to the user and is available for download once. After reset or download the session is cleared and the key cannot be recovered.

  The software is designed to make available the maximum cipher complexity (RSA 1068), however, due to JRE versions most machines have a max of 128. The software will utilise the strongest algorithms.

Features

* Choice of encryption METHODS
* Random password generator
* Session based to ensure machine can be cleaned
* Keylength based on encryption method Choice
* Can reach RSA 1068 file encryption standard (if JRE supports)
* Unit testing built in on applicaiton launch to validate ciphers and test sessions


Implementation stages

1. Backend development

  Started working on command line encryption. Used the standard Java.crypto API so got working fairly quickly.

2.  Basic graphical interface

  Attempted to deploy a basic UI. Encountered issues with selecting files and settled on FileChooser as the best method for finding targets and sources:

  '''
  FileChooser fileChooser = new FileChooser();
        //  Launches a dialogue window to the main scene
        selectedFile = fileChooser.showOpenDialog(null);
        //  Handle cases where the user aborts or the process fails
  '''

2. tests  
  Added automated security testing to the program which calls an abort if any of the conditions are not met. I decided to build this automated testing into the launch routine of the GIU as a feature of the platform. Perform some integrity checks. e.g. validate algorithms


3. Basic frontend functionality
  Model -> View -> Controller approach


4. Encryption functionality


5. Decryption functionality


6. Choice of METHODS


7. Password generator
  Utilised
   cryptographically strong random number generator (RNG). (Secure random) to generate secret phrases that are of the correct length based on the desired encryption algorithm.
