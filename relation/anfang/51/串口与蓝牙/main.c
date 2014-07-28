#include<reg52.h>
#define uchar unsigned char
uchar control[] = "takepic";
char temp, i, flag;
bit oldFlag = 1;
char con = 0;


void init(){
  TMOD = 0x20;
  TH1 = 0xfd;
  TL1 = 0xfd;
  TR1 = 1;
  SM0 = 0;
  SM1 = 1;
  EA = 1;
  ES = 1; 
  REN = 1;
}
void sendToES(){
   ES = 0;
   P1 &= 0xFD;
   for(i =0; i < 7; i++){
      SBUF = control[i];
      P1 &= 0xFB;
	  while(!TI);
	  TI = 0;
   }
   P1 &= 0xF7;
   ES = 1;
}

void main(){
  init();
  P0 = 0xFF;
  P1 = 0xFF;
  EA = 0;
  while(1){
     P1 &= 0x7F; 
     P0 = 0xFF; 
	 con = P0 & 0X1; 
	 if(0 == con ){  
	   if(oldFlag == 1){
         P1  &= 0XFE;
	     sendToES();
	   }
	 }
	 oldFlag = con;
  }
}

void ser() interrupt 4{
   RI = 0;
   P1 &= 0xBF;
}