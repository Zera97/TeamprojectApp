/**
*  	@filename   :   sim7x00.cpp
*  	@author     :   Alexander Luft u31702 & Dominik Dosdall u31701
*	@Date		:	31.08.2019
*/

#include "../arduPi.h"
#include "../sim7x00.h"

// Pin definition
int POWERKEY = 6;

int8_t answer;

void setup() {
	sim7600.PowerOn(POWERKEY);
	sim7600.MyGPSPositioning2();
}

void loop() {
	
}

int main() {
	setup();
	while (1) {
		loop();
	}
	return (0);
}
