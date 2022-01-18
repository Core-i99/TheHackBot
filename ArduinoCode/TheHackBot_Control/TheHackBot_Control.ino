/*
  HC05 - Bluetooth module
  © Stijn Rombouts - 2021
*/

#include <SoftwareSerial.h>
SoftwareSerial BTModule(3, 2); // RX & TX pins

const int ledUP = 13; // Built in LED in Arduino board
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
  BTModule.begin(9600);
  Serial.begin(9600); // Communication rate of the Bluetooth Module
}

void loop() {

  // To read message received from other Bluetooth Device
  if (BTModule.available() > 0) { // Check if there is data coming
    msg = BTModule.read(); // Read the message as String
    Serial.println("Android Command: " + msg);
    switch (msg) {
      case '1':
        digitalWrite(ledUP, 1); // Turn on LED
        BTModule.println("LEDUP is turned on\n"); // Then send status message to Android
        msg = ' ';
        break;
      case '2':
        digitalWrite(ledLEFT, 1); // Turn on LED
        BTModule.println("LEDLEFT is turned on\n"); // Then send status message to Android
        msg = ' ';
        break;
      case '3':
        digitalWrite(ledRIGHT, 1); // Turn on LED
        BTModule.println("LEDRIGHT is turned on\n"); // Then send status message to Android
        msg = ' ';
        break;
      case '4':
        digitalWrite(ledDOWN, 1); // Turn on LED
        BTModule.println("LEDDOWN is turned on\n"); // Then send status message to Android
        msg = ' ';
        break;
      case '5':
        digitalWrite(ledUP, 0); // Turn off LED
        digitalWrite(ledLEFT, 0); // Turn off LED
        digitalWrite(ledRIGHT, 0); // Turn off LED
        digitalWrite(ledDOWN, 0); // Turn off LED
        BTModule.println("LED's are turned off\n"); // Then send status message to Android
        msg = ' ';
        break;
     default:
        BTModule.println("That's not an option!");
        break;   
    }
  }
}
