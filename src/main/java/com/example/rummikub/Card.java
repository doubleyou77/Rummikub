package com.example.rummikub;

class Card{//말그대로 카드
    public static final String RESET = "\u001B[0m";

    //카드 색상바꾸고 싶으면 여기서 바꾸셈 ㅇㅇ.이름은 걍 냅둠.
    public static final String FONT_BLACK = "\u001B[30m";
    public static final String FONT_RED = "\u001B[31m";
    public static final String FONT_YELLOW = "\u001B[33m";
    public static final String FONT_BLUE = "\u001B[34m";

    int sort=-1;//0,1,2,3,4 순서대로 검정,빨강,노랑,파랑,히스레저검정,5면 히스레저 빨강
    int cardNumber=-1;//1~13 . 조커면 0
    String cardText;

    boolean byPlayer=false;

    public Card(int sort,int cardNumber) {
        this.sort=sort;
        this.cardNumber=cardNumber;

        {//카드 색상 텍스트 설정
            if(sort==0) {//삼항 안먹어서 이걸로 씀
                cardText=FONT_BLACK
                        + cardNumber+RESET+" ";
            }else if(sort==1) {
                cardText=FONT_RED
                        + cardNumber+RESET+" ";
            }else if(sort==2) {
                cardText=FONT_YELLOW
                        + cardNumber+RESET+" ";
            }else if(sort==3)  {
                cardText=FONT_BLUE
                        + cardNumber+RESET+" ";
            }else if(sort==4)  {
                cardText=FONT_BLACK
                        + "J"+RESET+" ";
            }else if(sort==5)  {
                cardText=FONT_RED
                        + "J"+RESET+" ";
            }
        }
    }
}



