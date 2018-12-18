/*
 *  This sketch demonstrates how to set up a simple HTTP-like server.
 *  The server will set a GPIO pin depending on the request
 *    http://server_ip/led/0 will set the GPIO2 low,
 *    http://server_ip/led/1 will set the GPIO2 high
 *  server_ip is the IP address of the ESP8266 module, will be 
 *  printed to Serial when the module is connected.
 */

#include <ESP8266WiFi.h>
#include <Servo.h>
Servo servo;
const char* ssid = "Goutham";
const char* password = "12345678";
int val=0;
int c=1;
String req="";

//String ip="172.20.10.8";
WiFiClient client ;

// Create an instance of the server
// specify the port to listen on as an argument
WiFiServer server(80);

//RainSensor
int rainsensor = 16;//D0
int rainsensorstate = 0;
int a = 5;

//temperature variables
int relaypin = 0; // D3
float resolution = 0.0024;

 //UltraSonic Sensor
 const int trigPin = 4;  //D2
const int echoPin = 5;  //D1
int Buzzer = 14; // D5
// defines variables
long duration;
int distance;

void setup() {
  
  
  Serial.begin(115200);
 //for rain sensor
  //serial.begin();
  Serial.println("Rain sensor");
  pinMode(rainsensor, INPUT);
  servo.attach(2); //D4
  servo.write(100);
  delay(2000);
  
  //For temp sensor
  //server.begin();
  Serial.println("Temp Sensor");
  pinMode(relaypin, OUTPUT);
  
  //For UltraSonic Sensor
  pinMode(trigPin, OUTPUT); // Sets the trigPin as an Output
pinMode(echoPin, INPUT); // Sets the echoPin as an Input
pinMode(Buzzer, OUTPUT);
 //serial.begin(9600);
  Serial.println("Ultrasonic Sensor");
  // prepare GPIO2
 // pinMode(ledPin, OUTPUT);
 // digitalWrite(ledPin, 0);

   digitalWrite(relaypin, HIGH);
  // Connect to WiFi network
  Serial.print("Connecting to ");
  Serial.println(ssid);
  
  WiFi.begin(ssid, password);
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected");
  
  // Start the server
  server.begin();
  Serial.println("Server started");

  // Print the IP address
   
  Serial.println(WiFi.localIP());
  
}

void loop() {


 //UltraSonic Sensor Code  
  // Clears the trigPin
digitalWrite(trigPin, LOW);
delayMicroseconds(2);

// Sets the trigPin on HIGH state for 10 micro seconds
digitalWrite(trigPin, HIGH);
delayMicroseconds(10);
digitalWrite(trigPin, LOW);

// Reads the echoPin, returns the sound wave travel time in microseconds
duration = pulseIn(echoPin, HIGH);

// Calculating the distance
distance= duration*0.034/2;
// Prints the distance on the Serial Monitor
Serial.print("Distance: ");
Serial.println(distance);
if (distance >= 20 || distance <= 8) 
        {
         Serial.println("no object detected");
        digitalWrite(Buzzer,LOW);
        }
  else {
       Serial.println("object detected \n");
        Serial.print("distance= ");              
        Serial.println(distance);        //prints the distance if it is between the range 0 to 200
        //tone(Buzzer,400); 
        digitalWrite(Buzzer,HIGH);
       
  }
  delay(2000);
    // Check if a client has connected

    
 client = server.available();
  if (!client) {
    Serial.println("No Client");
   // delay(2000);
    return;
  }
 
  // Wait until the client sends some data
  Serial.println("new client");
  c=1;
  while(!client.available()){
    Serial.println("client not available");
    delay(1000);
  }
  
  // Read the first line of the request
 
 Serial.println("outside while");

while(c<10){
     Serial.println("inside while");
     req = client.readStringUntil('\r');
  Serial.println(req);

  Serial.println("Got Request!!!");
 
//UltraSonic Sensor Code  
  // Clears the trigPin


  
//INDEXES 


 if (req.indexOf("/led/2") != -1)
 {//val = 0;   
  digitalWrite(relaypin, LOW);
 delay(200);
 }  
else if (req.indexOf("/led/3") != -1)
{//val = 1;  
 digitalWrite(relaypin, HIGH);
 delay(200);
}   

else if (req.indexOf("/led/4") != -1)
{ // val = 4;
servo.write(0);
}
else if (req.indexOf("/led/5") != -1)
{  //  val = 5;
servo.write(100);
}
else if (req.indexOf("/led/11") != -1){
  val=11;
  servo.write(100);  
  Serial.println("Exiting");
   c=20;
    client.stop();
  //This Should be empty!!
}
else if (req.indexOf("/led/10") != -1 || val==10)
{ 
val = 10;
//RAIN SENSOR
client = server.available();
rainsensorstate = digitalRead(rainsensor); // put your main code here, to run repeatedly:
  if (a != rainsensorstate) 
  {
    if (rainsensorstate == 0)
    {
      //digitalWrite(ledpin, HIGH);
      delay(200);
      servo.write(50);
      a = rainsensorstate;
      delay(5000);

      //servo.write(90);
      Serial.println("Yes");
      

      //delay(1000);
      }
      else
      { 
        //digitalWrite(ledpin, LOW);
        servo.write(10);
        Serial.println("No");
        delay(5000);
        a = rainsensorstate;
      }
    }
//PDLC FILM & TEMPERATURE SENSOR
float temperature = analogRead(A0);
 temperature = (temperature*resolution);
 temperature = temperature*100.0;
Serial.print("LM35 temperature: ");
 Serial.println(temperature);
  if (temperature>=100.00)
  {Serial.println("Temp is high");
   digitalWrite(relaypin, LOW); 
   delay(200);
 }
 else {
   Serial.println("Temp is Low");
   digitalWrite(relaypin, HIGH); 
   delay(200);
   
 }
   delay(1000);
}
 else {
    Serial.println("invalid request");
    c=20;
    //client.stop();
  }

  // Set GPIO2 according to the request
 // digitalWrite(ledPin, val);
  
  

   // Return the response
  client.println("HTTP/1.1 200 OK");
  client.println("Content-Type: text/html");
  client.println(""); //  do not forget this one
  client.print("Command Passed! ");
 
//  if(val == 0) {
   // client.print("Off");
 // } else {
 //   client.print("Off");
 // }
 // Serial.println("Client disonnected");

  // The client will actually be disconnected 
  // when the function returns and 'client' object is detroyed
  
 }

//function();
}
