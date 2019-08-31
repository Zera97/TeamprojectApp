// Client side C/C++ program to demonstrate Socket programming 
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
#define PORT 8080 

int main(int argc, char const *argv[]) 
{ 
	
	struct sockaddr_in address; 
	int sockId = 0; 
	char *hello = "Hello from client"; 
	char buffer[1024] = {0}; 
	socklen_t adrlen = 0;
	
	if ((sockId = socket(AF_INET, SOCK_STREAM, 0)) < 0) 
	{ 
		printf("\n Socket creation error \n"); 
		return -1; 
	} 
	
	address.sin_family = AF_INET;
    struct hostent *dns = gethostbyname("eos-noctis.de");
    address.sin_addr.s_addr = inet_addr(inet_ntoa(*((struct in_addr *)dns->h_addr_list[0])));
    address.sin_port = htons(31896);
    adrlen = sizeof(address);

	//memset(&serv_addr, '0', sizeof(serv_addr)); 


	if (connect(sockId, (struct sockaddr *)&address, adrlen) < 0) 
	{ 
		printf("\nConnection Failed \n"); 
		return -1; 
	} 
	send(sockId , hello , strlen(hello) , 0 ); 
	printf("Hello message sent\n"); 
	printf("%s\n",buffer ); 
	close(sockId); 
	return 0; 
} 
