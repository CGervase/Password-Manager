# Password Manager

### About
Password Manager is a standalone desktop application that stores and retrieves your passwords. With the boundless growth of the Internet and the digital migration of various services, login information begins to pile up and sometimes we can't quite remember our credentials for a website we only use once a year. Rather than going through the tedious "Forgot my Username/Password" process, Password Manager allows hassle-free storage of passwords.

The application is written in Java, utilizing JavaFX and FXML for the design of the interface. The basic functionality of this application is complete, though it is by no means a finished product (see the **Future Updates** section for more details). Ultimately, this application isn't intended to compete with the other password managers out there, and doesn't (yet) promise the degree of security that is typically desired in a professional password manager; rather, it is intended to be a somewhat-practical personal project to improve in and learn about Java development.

### How to Use
##### From the command line:
1. Clone the repository: `git clone https://github.com/CGervase/repository-name.git`
2. Navigate to the `application` folder
3. Compile the classes (6 total): `javac Main.java PassMgrLoginController.java PassMgrController.java User.java Password.java PasswordGenerator.java`
4. Navigate to the `src` folder
5. Run the Java application: `java Main`

Alternatively, download the .jar file [here](https://github.com/CGervase/Password-Manager).

### Future Updates
##### Encryption
Currently, the credentials stored by the application are kept in simple text files. Ideally, they will be encrypted using Java's Cipher class upon exit of the application and decrypted upon opening the application (for user/master password) and login (for user-specific passwords).
##### Bug Fixes
Bugs will be fixed as they arise. (Pretty self-explanatory.)