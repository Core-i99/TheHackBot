#include <SoftwareSerial.h>
SoftwareSerial MyBTModule(3, 2); // RX & TX pins

const int LEDUP = 12; // Built in LED in Arduino board
const int LEDLEFT = 11;
const int LEDRIGHT = 10;
const int LEDDOWN = 9;
String msg, cmd;

void setup() {
  // Initialization
  pinMode(LEDUP, OUTPUT);
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
  if (msg == "<up>") {
    digitalWrite(LEDUP, 1); // Turn on LED
    MyBTModule.println("LEDUP is turned on\n"); // Then send status message to Android
    msg = ""; // reset command
  }

  if (msg == "<stop>") {
    digitalWrite(LEDUP, 0); // Turn off LED
    digitalWrite(LEDLEFT, 0);
    digitalWrite(LEDRIGHT, 0);
    digitalWrite(LEDDOWN, 0);
    MyBTModule.println("LED's are turned off\n");
    msg = ""; // reset command
  }
  if (msg == "<down>") {
    digitalWrite(LEDDOWN, 1);
    MyBTModule.println("LEDDOWN is turned on\n"); // Then send status message to Android
    msg = ""; // reset command
  }
  if (msg == "<left>") {
    digitalWrite(LEDLEFT, 1);
    MyBTModule.println("LEDLEFT is turned on\n"); // Then send status message to Android
    msg = ""; // reset command
  }
  if (msg == "<right>") {
    digitalWrite(LEDRIGHT, 1);
    MyBTModule.println("LEDDRIGHT is turned on\n"); // Then send status message to Android
    msg = ""; // reset command
  }


}
