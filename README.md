# strongbox
Java based graphical application for simple document encryption


Purpose
  Simple file encryption software that gives the user granular control over type of file encryption.
  Prevent insecure passwords by generating the seed randomly
  Return encryption key


Implementation stages

1. Backend development


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
