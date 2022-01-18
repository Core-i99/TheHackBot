/*
  HC05 - Bluetooth module
  © Stijn Rombouts - 2021
*/

/*
  Code used to control 1 led
*/

#include <SoftwareSerial.h>
SoftwareSerial BTModule(3, 2); // RX & TX pins

const int led = 13;

char msg;

void setup() {
  // Initialization
  pinMode(led, OUTPUT);
  BTModule.begin(9600); // Communication rate of the Bluetooth Module
  Serial.begin(9600);
}

void loop() {
  // Read message received from other Bluetooth Device
  if (BTModule.available() > 0) { // Check if there is data coming
    msg = BTModule.read(); // Read the message
    switch (msg) {
      case '1':
        digitalWrite(led, 1); // Turn on LEDUP
        BTModule.println("LED is turned on\n"); // Send status message to Android
        break;
      case '2':
        digitalWrite(led, 0); // Turn on LEDLEFT
        BTModule.println("LED is turned off\n"); // Send status message to Android
        break;
      default:
        BTModule.println("That's not an option!");
        break;
    }
  }
}
