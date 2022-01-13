/*   
HC05 - Bluetooth module 
© Stijn Rombouts - 2021
*/ 

#include <SoftwareSerial.h>
SoftwareSerial MyBTModule(3, 2); // RX & TX pins

const int ledPin = 13; // Built in LED in Arduino board

String msg, cmd;

void setup() {
  // Initialization
  pinMode(ledPin, OUTPUT);
  digitalWrite(ledPin, LOW);
  MyBTModule.begin(9600);
  Serial.begin(9600); // Communication rate of the Bluetooth Module
  msg = "";
}

void loop() {

  // To read message received from other Bluetooth Device
  if (MyBTModule.available() > 0) { // Check if there is data coming
    msg = MyBTModule.readString(); // Read the message as String
    Serial.println("Android Command: " + msg);
  }

  //TODO: make switch
  if (msg == "<up>") {
    digitalWrite(ledPin, HIGH); // Turn on LED
    MyBTModule.println("LEDUP is turned on\n"); // Then send status message to Android
    msg = ""; // reset command
  }
  else if (msg == "<left>") {
    digitalWrite(ledPin, HIGH); // Turn on LED
    MyBTModule.println("LEDLEFT is turned on\n"); // Then send status message to Android
    msg = ""; // reset command
  }
  else if (msg == "<right>") {
    digitalWrite(ledPin, HIGH); // Turn on LED
    MyBTModule.println("LEDRIGHT is turned on\n"); // Then send status message to Android
    msg = ""; // reset command
  }
  else if (msg == "<down>") {
    digitalWrite(ledPin, HIGH); // Turn on LED
    MyBTModule.println("LEDDOWN is turned on\n"); // Then send status message to Android
    msg = ""; // reset command
  }
  else if (msg == "<stop>") {
    digitalWrite(ledPin, LOW); // Turn off LED
    MyBTModule.println("LED's are turned off\n"); // Then send status message to Android
    msg = ""; // reset command
  }
}
