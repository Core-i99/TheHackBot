/*
  HC05 - Bluetooth module
  © Stijn Rombouts - 2021
*/

#include <SoftwareSerial.h>
SoftwareSerial MyBTModule(3, 2); // RX & TX pins

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
  MyBTModule.begin(9600);
  Serial.begin(9600); // Communication rate of the Bluetooth Module
}

void loop() {

  // To read message received from other Bluetooth Device
  if (MyBTModule.available() > 0) { // Check if there is data coming
    msg = MyBTModule.read(); // Read the message as String
    Serial.println("Android Command: " + msg);
  }

  //TODO: make switch
  if (msg == '1') {
    digitalWrite(ledUP, HIGH); // Turn on LED
    MyBTModule.println("LEDUP is turned on\n"); // Then send status message to Android
    msg = ' ';
  }
  else if (msg == '2') {
    digitalWrite(ledLEFT, HIGH); // Turn on LED
    MyBTModule.println("LEDLEFT is turned on\n"); // Then send status message to Android
    msg = ' ';
  }
  else if (msg == '3') {
    digitalWrite(ledRIGHT, HIGH); // Turn on LED
    MyBTModule.println("LEDRIGHT is turned on\n"); // Then send status message to Android
    msg = ' ';
  }
  else if (msg == '4') {
    digitalWrite(ledDOWN, HIGH); // Turn on LED
    MyBTModule.println("LEDDOWN is turned on\n"); // Then send status message to Android
    msg = ' ';
  }
  else if (msg == '5') {
    digitalWrite(ledUP, LOW); // Turn off LED
    digitalWrite(ledLEFT, LOW); // Turn off LED
    digitalWrite(ledRIGHT, LOW); // Turn off LED
    digitalWrite(ledDOWN, LOW); // Turn off LED
    MyBTModule.println("LED's are turned off\n"); // Then send status message to Android
    msg = ' ';
  }
}
