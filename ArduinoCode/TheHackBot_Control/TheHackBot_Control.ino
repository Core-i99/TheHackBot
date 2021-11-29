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

  // Control LED in Arduino board
  if (msg == "<turn on>") {
    digitalWrite(ledPin, HIGH); // Turn on LED
    MyBTModule.println("LED is turned on\n"); // Then send status message to Android
    msg = ""; // reset command
  }

  if (msg == "<turn off>") {
    digitalWrite(ledPin, LOW); // Turn off LED
    MyBTModule.println("LED is turned off\n"); // Then send status message to Android
    msg = ""; // reset command
  }
  if (msg == "<down>") {
    MyBTModule.println("recieved down\n"); // Then send status message to Android
    msg = ""; // reset command
  }
  if (msg == "<left>") {
    MyBTModule.println("recieved left\n"); // Then send status message to Android
    msg = ""; // reset command
  }
  if (msg == "<right>") {
    MyBTModule.println("recieved right\n"); // Then send status message to Android
    msg = ""; // reset command
  }


}
