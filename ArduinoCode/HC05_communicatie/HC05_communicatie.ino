/*   
HC05 - Bluetooth module 
© Stijn Rombouts - 2021
*/ 

#include <SoftwareSerial.h> 
SoftwareSerial MyBTModule(3, 2); // RX & TX pins 
String msg;

void setup() 
{   
 Serial.begin(9600); 
 MyBTModule.begin(9600); 
} 


void loop() 
{ 
 if (MyBTModule.available() > 0) {
   msg = MyBTModule.readString();
   Serial.println("Arduino recieve: " + msg);
 }
 
 if (Serial.available()) 
     MyBTModule.write(Serial.read());
}  
