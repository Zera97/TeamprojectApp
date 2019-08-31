/**
*  	@filename   :   sim7x00.cpp
*  	@author     :   Alexander Luft u31702 & Dominik Dosdall u31701
*	@Date		:	31.08.2019
*/


#include "sim7x00.h"
#include "arduPi.h"
#include "json.hpp"
#include <string>
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <arpa/inet.h>
#include <string.h>
#include <netdb.h>
#include <codecvt>

using json = nlohmann::json;
using namespace std;

Sim7x00::Sim7x00(){
}
Sim7x00::~Sim7x00(){
}

/**************************Power on Sim7x00**************************/
/* Sarten des GPS-Moduls und Verbindung zum Serialport des Raspberry Pi */
void Sim7x00::PowerOn(int PowerKey = powerkey){
   uint8_t answer = 0;

	Serial.begin(115200); // Öffnet Serial port mit einer Datenrrate von 115200 bps
	
	SetSimPin(); // Setzen der Sim-Pin auf dem Modul

	// checks if the module is started
	answer = sendATcommand("AT", "OK", 2000);
	if (answer == 0)
	{
		printf("Starting up...\n");

		
		pinMode(PowerKey, OUTPUT);
		printf("pinMode...\n");

		// power on pulse
		digitalWrite(PowerKey, HIGH);
		printf("digitalWrite (HIGH)...\n");

		delay(600);
		digitalWrite(PowerKey, LOW);
		printf("digitalWrite (LOW)...\n");

		// waits for an answer from the module
		while (answer == 0) {     // Send AT every two seconds and wait for the answer
			answer = sendATcommand("AT", "OK", 2000);
		}
	}

	delay(5000);
	
	//SetSimPin();

	// Solange wie keine Verbindung zum Modul erstellt werden kann, wird die Anfrage zum Modul immer wieder gestellt
	while ((sendATcommand("AT+CREG?", "+CREG: 0,1", 500) || sendATcommand("AT+CREG?", "+CREG: 0,5", 500)) == 0)
		delay(500);

}

/**************************SET SIM PIN**************************/
/* Automatisches Setzen der SIM-Pin */
void Sim7x00::SetSimPin(){
    uint8_t answer = 0;
    int i = 0;
    char status[15];
    char ready[] = "READY";
    
	// Abfrage ob eine Pin benötigt wird
    answer = sendATcommand("AT+CPIN?", "+CPIN: ", 2000);
    if(answer == 1){
	
	answer = 0;
	
	 // Liest die Antwort vom Serial Port ein und legt sie in einem Char-Array ab
	 while(Serial.available() == 0);
            do{
                if(Serial.available() > 0 ){  
                    status[i] = Serial.read(); 
                    i++;
                    if (strstr(status, "OK") != NULL)    
                    {
                        answer = 1;		
                    }
                }			
            }while(answer == 0);    // Waits for the asnwer with time out
            
            status[i] = '\0';
            
            printf("%s\n",status); 
	    
	    status[5]='\0';
	    
		// Wenn die Antwort des Serial-Ports "READY" lautet, ist keine Pin eingabe erforderlich	
	    if(strcmp(status, ready) == 0){
		printf("Keine PIN Eingabe erforderlich \n");
		delay(2000);
	    }
		//Sollte die Antwort nicht "READY" lauten, wird ein AT-Command an das Modul mit der PIN der Simkarte gesendet und dadrtuch die SIMkarte entspeert
	    else{
		sendATcommand("AT+CPIN=3367", "OK", 1000);
		printf("PIN gesetzt \n");
		 delay(10000); 
	    }      
    }
}
/**************************MyGPS positoning2**************************/
/* Ermitteln der GPS-Position durch das GPS-Modul, schreiben der Empfangen Daten in einen JSON-String und an die Middleware senden*/
bool Sim7x00::MyGPSPositioning2(){

    uint8_t answer = 0;
    bool RecNull = true;
    int i = 0;
    int uid = 1;
    char* pch;
    char RecMessage[200];
    char LatDD[2],LatMM[9],LogDD[3],LogMM[9],DdMmYy[6] ,UTCTime[6], freq[2];
    int DayMonthYear;
    float Lat,Log;
    FILE *fp;
    json jsonData;
    
    printf("Start GPS session...\n");
    sendATcommand("AT+CGPS=1,1", "OK:", 1000);    // startet GPS session, standalone mode

    delay(2000);

    while(RecNull)
    {
        answer = sendATcommand("AT+CGPSINFO", "+CGPSINFO: ", 1000);    // startet GPS session, standalone mode
        if (answer == 1)
        {
	    
            answer = 0;
            while(Serial.available() == 0);
            // diese schleife liest die Antwort des  Serial-Port 
            do{
                if(Serial.available() > 0 ){   
                    RecMessage[i] = Serial.read(); 
                    i++;
                    // Prüft ob die Antwoert den wert "OK" leifert
                    if (strstr(RecMessage, "OK") != NULL)    
                    {
                        answer = 1;
                    }
                }
				
            }while(answer == 0);    // Wartet auf die Antwort mit Timeout           
            RecMessage[i] = '\0';            
            printf("%s\n",RecMessage);
			// Wenn keien Daten empfangen werden, wird nichts gespeichert
            if (strstr(RecMessage, ",,,,,,,,") != NULL) 
            {
                memset(RecMessage, '\0', i);    // Initialize the string
                RecNull = true;
				printf("RecNull = true...\n");
                i = 0;
                answer = 0;
                delay(1000);
            }
			// Sollte das Modul GPS-Daten Empfangen und die RecMEssage nicht leer sein, dann wird diese aufgesplittet in Verschiede Nachrichtenteile
            else
            {
				strncpy(LatDD,RecMessage,2);
				strncpy(LatMM,RecMessage+2,9);
				Lat = atoi(LatDD) + (atof(LatMM)/60);

				strncpy(LogDD,RecMessage+14,3);
				strncpy(LogMM,RecMessage+17,9);
				Log = atoi(LogDD) + (atof(LogMM)/60);
							
				string latString;
				latString = to_string(Lat);
				
				string logString;
				logString = to_string(Log);

				strncpy(DdMmYy,RecMessage+29,6);
				DdMmYy[6] = '\0';

				strncpy(UTCTime,RecMessage+36,6);
				UTCTime[6] = '\0';
					
				printf("DDMMYY: %s",DdMmYy);
				printf(" UTC: %s",UTCTime);
				// Längengrad und Höhengrade werden in Nord und Sud, sowie Ost und West aufgeteilt
				if(RecMessage[12] == 'N')
					printf(" Lat: %f,N",Lat);
				else if(RecMessage[12] == 'S')
					printf(" Lat: %f,S",Lat);
				else
					return false;
				if(RecMessage[27] == 'E')
					printf(" Long: %f,E",Log);
				else if(RecMessage[27] == 'W')
					printf(" Long: %f,W",Log);
				else
					return false;
				printf("\n");
				
				fp = fopen("/home/pi/GPS/Data/werte.txt", "a");
		
				if(fp ==NULL){
					printf("Datei konnte nicht geoeffnet werden.\n");
				}
				else{
					fseek(fp,0,SEEK_END);
					string breite;
					string laenge;
					
					// Längengrad und Höhengrade werden in Nord und Sud, sowie Ost und West aufgeteilt
					if(RecMessage[12] == 'N')
					breite = "N";
					else if(RecMessage[12] == 'S')
					breite = "S";
					else
					return false;
					
					if(RecMessage[27] == 'E')
					laenge = "E";
					else if(RecMessage[27] == 'W')
					laenge = "W";
					else
					return false;
					
					// Ermitteln der Frequenzstärke
					GetFrequence(freq);
							
					// Speichern der einzelnen GPS-Informations Elemente in einem JSON-String			
					jsonData = {
					 {"id", uid},
					 {"code", 1},
					 {"sender", "BOX"},
					 {"boxId", 1},
					 {"line", "204"},
					 {"latitude", latString},
					 {"longitude", logString},
					 {"signal", freq},
					 {"nettype", "NULL"}
					};						
					string s = jsonData.dump();
					
					printf("JSON: %s \n", s.c_str());
					
					fprintf(fp,"JSON: %s ", s.c_str());
					fprintf(fp," DDMMYY: %s",DdMmYy);
					fprintf(fp," UTC: %s \n",UTCTime);
					
					printf("Werte wurden geschrieben.\n");
					fclose(fp);
					
					int stringLength = s.length();
					printf("String.length() %d\n",i );
	
					delay(2500);
					
					// Öffnen der Internetverbindung über das Modul
					string command_on = "sudo pon&";
					system(command_on.c_str());
					
					delay(3500);
					
					// Versenden des JSON-String über ienen TCP-Client
					TcpClient(s.c_str(), stringLength);
					
					delay(3000);
					
					// Schließen der Internetverbindung über das Modul
					string command_off = "sudo poff&";
					system(command_off.c_str());
				
					uid++;
					
					latString.clear();
					logString.clear();  		    
				}
		
				// Leeren des Serial.Port Speichers
				Serial.flush();
				i = 0;
				answer = 0;
				
				// Leeren des RecMessage Speichers
				memset(RecMessage,0,sizeof RecMessage);
				//RecNull = false;

            } 
        }
        else
        {
            printf("error %o\n",answer);
            sendATcommand("AT+CGPS=0", "OK:", 1000);
            return false;
        }
	delay(3000);
    }
	return true;
}

/**************************GetFrequence**************************/

char Sim7x00::GetFrequence(char freq[]){
    
    uint8_t answer2 = 0;
    bool RecNull = true;
    int i = 0;
    char RecMes[100];
    char frequence[2];
    
    printf("Start frequence session...\n");

        answer2 = sendATcommand("AT+CSQ", "+CSQ: ", 1000); 
	
	delay(2000);				   // start GPS session, standalone mode
        if (answer2 == 1)
        {
	    
            answer2 = 0;
            while(Serial.available() == 0);
            do{
                // if there are data in the UART input buffer, reads it and checks for the asnwer
                if(Serial.available() > 0 ){
		   // printf("Serial.available() > 0 \n");   
                    RecMes[i] = Serial.read(); 
                    i++;
		   	
                    // check if the desired answer (OK) is in the response of the module
                    if (strstr(RecMes, "OK") != NULL)    
                    {
			
                        answer2 = 1;
			
                    }
                }
				
            }while(answer2 == 0);    // Waits for the asnwer with time out
            
	    
            RecMes[i] = '\0';
            
            printf("%s\n",RecMes);
	    
	    strncpy(freq,RecMes,2);
	    
	    return 0;
	    
	     
	    
	}
}

/**************************Tcp Client over WLAN/LAN**************************/
/* Öffnen einer TCP verbindung zur Middleware und versenden der GPS-Daten*/
int Sim7x00::TcpClient(const char* string, int stringLength){
    
	struct sockaddr_in address; 
	int sockId = 0; 
	//char *hello = "Hello from client"; 
	char buffer[512] = {0}; 
	char *in;
	socklen_t adrlen = 0;
	
	if ((sockId = socket(AF_INET, SOCK_STREAM, 0)) < 0) 
	{ 
	    printf("\n Socket creation error \n"); 
	    return -1; 
	} 
	else{
	    printf("\n Socket wurde erstellt \n");
	}
	
	if ((address.sin_family = AF_INET) < 0) 
	{ 
	    printf("\n Adress.sin_family registriert creation error \n"); 
	    return -1; 
	} 
	else{
	    printf("\n Adress.sin_family registriert \n");
	    struct hostent *dns = gethostbyname("eos-noctis.de");
	    printf("\n 1111111111 \n");
	    if (system("ping -c 2 8.8.8.8 2>&1 >/dev/null") != 0) 
	    { 
		printf("\n kein Internet \n"); 
		return -1; 
	    } 
	    else{
		printf("\n Internet \n");
		
		address.sin_addr.s_addr = inet_addr(inet_ntoa(*((struct in_addr *)dns->h_addr_list[0])));
		printf("\n 2222222222222 \n");
		address.sin_port = htons(31896);
		printf("\n 33333333333333 \n");
		adrlen = sizeof(address);
		printf(" Adressierung wurde konfiguriert \n");
	    }

	}

	if (connect(sockId, (struct sockaddr *)&address, adrlen) < 0) 
	{ 
	    printf("\nConnection Failed \n"); 
	    return -1; 
	}
	else{
	    printf("\n connection hergestellt \n");
	} 
	
	if(send(sockId , string , stringLength , 0 ) < 0)
	{
	    printf("Sending Failed \n"); 
	    return -1; 
	}
	else{
	    printf("JESON message sent\n"); 
	    printf("JSON in SOCKET: %s \n", string);
	    
	} 
	
	shutdown(sockId, SHUT_WR);
	
	if(recv(sockId, buffer, 512, 0)< 0)
	{
	    printf("\recv Failed \n"); 
	    return -1; 
	}
	else{
	    printf("Recv  erhalten sent\n"); 
	    buffer[512] = '\0';
	    printf("Antwort: %s \n", buffer);
	}  
	
	printf("String.length() %d\n",stringLength );
	unistd::close(sockId); 
	printf("Socket geschlossen \n");
}

/**************************Other functions**************************/
char Sim7x00::sendATcommand(const char* ATcommand, unsigned int timeout) {
	uint8_t x = 0, answer = 0;
	char response[100];
	unsigned long previous;
	memset(response, '\0', 100);    // Initialize the string

	delay(100);

	while (Serial.available() > 0) Serial.read();    // Clean the input buffer

	Serial.println(ATcommand);    // Send the AT command 

	previous = millis();

	// this loop waits for the answer
	do {
		// if there are data in the UART input buffer, reads it and checks for the asnwer
		if (Serial.available() != 0) {
			response[x] = Serial.read();
			printf("%c", response[x]);
			x++;
		}
		
	} while ((answer == 0) && ((millis() - previous) < timeout));

	return answer;
}


char Sim7x00::sendATcommand(const char* ATcommand, const char* expected_answer, unsigned int timeout) {

	char x = 0, answer = 0;
	char response[100];
	unsigned long previous;

	memset(response, '\0', 100);    // Initialize the string

	delay(100);

	while (Serial.available() > 0) Serial.read();    // Clean the input buffer

	Serial.println(ATcommand);    // Send the AT command 


	x = 0;
	previous = millis();

	// this loop waits for the answer
	do {
		if (Serial.available() != 0) {
			// if there are data in the UART input buffer, reads it and checks for the asnwer
			response[x] = Serial.read();
			printf("%c", response[x]);
			x++;
			// check if the desired answer  is in the response of the module
			if (strstr(response, expected_answer) != NULL)
			{
				printf("\n");
				answer = 1;
			}
		}
	}
	// Waits for the asnwer with time out
	while ((answer == 0) && ((millis() - previous) < timeout));

	return answer;
}

Sim7x00 sim7600 = Sim7x00();

