#include<reg52.h>
char temp;
void main(){
  P0 = 0XFF;
  P1 = 0XFF;
  while(1){
     //temp = (0XFD | P1 << 1);
	 //P1 = temp;
	 //P1 = (0XFD | P1 << 1);
	 P1 = P0;
  }
}