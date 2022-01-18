/*
  HC05 - Bluetooth module
  © Stijn Rombouts - 2021
*/

/*
  Code used to control 4 led's
*/

#include <SoftwareSerial.h>
SoftwareSerial BTModule(3, 2); // RX & TX pins

const int ledUP = 13;
const int ledLEFT = 12;
const int ledRIGHT = 10;
const int ledDOWN = 11;

char msg;

void setup() {
  // Initialization
  pinMode(ledUP, OUTPUT);
  pinMode(ledLEFT, OUTPUT);
  pinMode(ledRIGHT, OUTPUT);
  pinMode(ledDOWN, OUTPUT);
  BTModule.begin(9600); // Communication rate of the Bluetooth Module
  Serial.begin(9600);
}

void loop() {
  // Read message received from other Bluetooth Device
  if (BTModule.available() > 0) { // Check if there is data coming
    msg = BTModule.read(); // Read the message
    switch (msg) {
      case '1':
        digitalWrite(ledUP, 1); // Turn on LEDUP
        BTModule.println("LEDUP is turned on\n"); // Send status message to Android
        break;
      case '2':
        digitalWrite(ledLEFT, 1); // Turn on LEDLEFT
        BTModule.println("LEDLEFT is turned on\n"); // Send status message to Android
        break;
      case '3':
        digitalWrite(ledRIGHT, 1); // Turn on LEDRIGHT
        BTModule.println("LEDRIGHT is turned on\n"); // Send status message to Android
        break;
      case '4':
        digitalWrite(ledDOWN, 1); // Turn on LEDDOWN
        BTModule.println("LEDDOWN is turned on\n"); // Send status message to Android
        break;
      case '5':
        digitalWrite(ledUP, 0); // Turn off LEDUP
        digitalWrite(ledLEFT, 0); // Turn off LEDLEFT
        digitalWrite(ledRIGHT, 0); // Turn off LEDRIGHT
        digitalWrite(ledDOWN, 0); // Turn off LEDDOWN
        BTModule.println("LED's are turned off\n"); // Send status message to Android
        break;
      default:
        BTModule.println("That's not an option!");
        break;
    }
  }
}
