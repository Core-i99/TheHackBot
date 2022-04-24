/*
 * HC05 - Bluetooth module
 * Copyright (C) 2022 Stijn Rombouts
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

#include <SoftwareSerial.h>
SoftwareSerial BTModule(3, 2); // RX & TX pins

const int M2_1 = 11; //L293D IN4 - RIGHT FORWARD
const int M2_2 = 10; //L293D IN3 - RIGHT BACK
const int M1_1 = 6; //L293D IN1 - LEFT BACK
const int M1_2 = 5; //L293D IN2 - LEFT FORWARD

char msg;

void setup() {
  // Initialization
  pinMode(M2_1, OUTPUT);
  pinMode(M2_2, OUTPUT);
  pinMode(M1_1, OUTPUT);
  pinMode(M1_2, OUTPUT);
  BTModule.begin(9600); // Communication rate of the Bluetooth Module
  Serial.begin(9600);
}

void loop() {
  // Read message received from other Bluetooth Device
  if (BTModule.available() > 0) { // Check if there is data coming
    msg = BTModule.read(); // Read the message
    switch (msg) {
      case '1': //forward
        analogWrite(M1_2, 100); 
        analogWrite(M2_1, 100); 
        BTModule.println("FORWARD\n"); // Send status message to Android
        break;
      case '2': //left
        analogWrite(M2_1, 70); 
        BTModule.println("LEFT\n"); // Send status message to Android
        break;
      case '3': //right
        analogWrite(M1_2, 70); 
        BTModule.println("RIGHT\n"); // Send status message to Android
        break;
      case '4': //back
        analogWrite(M2_2, 100); 
        analogWrite(M1_1, 100); 
        BTModule.println("BACKWARDS\n"); // Send status message to Android
        break;
      case '5': //stop
        digitalWrite(M1_1, 0); 
        digitalWrite(M1_2, 0); 
        digitalWrite(M2_1, 0); 
        digitalWrite(M2_2, 0); 
        BTModule.println("STOP\n"); // Send status message to Android
        break;
      default:
        BTModule.println("That's not an option!");
        break;
    }
  }
}
