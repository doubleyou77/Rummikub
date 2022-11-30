package com.example.rummikub;

import java.util.ArrayList;
import java.util.Scanner;

public class Base {

    private static ArrayList<Card> Cards = new ArrayList<Card>();//현재 덱 카드
    private static Board curBoard;//현재 보드
    private static ArrayList<Card> playerCards = new ArrayList<Card>();//첫번째 플레이어 카드
    private static ArrayList<Card> playerCards2 = new ArrayList<Card>();//두번째 플레이어 카드

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        System.out.println("루미큐브");
        System.out.println("1 - 2인 플레이 , 2 - AI 2인 플레이 , 아무 숫자 - 종료");
        {//입력
            while (true) {
                Scanner sc = new Scanner(System.in);
                int input = sc.nextInt();
                if (input == 1) {
                    gameStart1();
                } else if (input == 2) {
                    gameStart2();
                } else if (input == 3){
                  gameStart3();
                } else {
                    break;
                }
            }
        }
        System.out.println("게임 종료라네여");
    }


    static void gameInit() {//게임 초기세팅
        Cards.clear();

        {//카드 세팅 ㅇㅇ
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 2; k++) {
                    for (int i = 1; i <= 13; i++) {
                        Cards.add(new Card(j, i));
                    }
                }
            }
            Cards.add(new Card(4, 0));
            Cards.add(new Card(5, 0));
        }


        {//보드 세팅
            curBoard = new Board();
        }


        {//플레이어1 카드세팅 45 -> 14 수정하기
            playerCards.clear();
            playerCards2.clear();
            for (int i = 0; i < 45; i++) {
                int drawIndex = (int) (Math.random() * Cards.size());
                playerCards.add(Cards.get(drawIndex));
                Cards.remove(drawIndex);
            }
            for (int i = 0; i < 45; i++) {
                int drawIndex = (int) (Math.random() * Cards.size());
                playerCards2.add(Cards.get(drawIndex));
                Cards.remove(drawIndex);
            }
        }

    }

    static boolean player1First = false;
    static boolean player2First = false;
    static boolean gameEnd = false;

    static void gameStart1() {//2인플
        gameInit();//게임 초기화

        int whoPlayer = 1;//1,2. 현재 턴 누군지
        player1First = false;//30개 이상인지 체크할려고 씀
        player2First = false;
        while (true) {
            {//


                {//입력.

                    //세이브
                    ArrayList<Card> playerCardsTemp = new ArrayList<Card>();
                    {//세이브
                        for (int i = 0; i < playerCards.size(); i++) {
                            playerCardsTemp.add(playerCards.get(i));
                        }
                        curBoard.saveBoard();
                    }

                    //내가 낸건지 확인 변수 초기화
                    for (int i = 0; i < playerCards.size(); i++) {
                        playerCards.get(i).byPlayer = false;
                    }
                    for (int i = 0; i < playerCards2.size(); i++) {
                        playerCards2.get(i).byPlayer = false;
                    }
                    for (int i = 0; i < curBoard.boardCards.size(); i++) {
                        for (int j = 0; j < curBoard.boardCards.get(i).size(); j++) {
                            curBoard.boardCards.get(i).get(j).byPlayer = false;
                        }
                    }

                    while (true) {//한 턴
                        System.out.println();

                        System.out.println("현재 보드");
                        curBoard.boardPrint();
                        System.out.println((whoPlayer == 1) ? "플레이어 1 - 카드" : "플레이어 2 - 카드");
                        curBoard.CheckCards((whoPlayer == 1) ? playerCards : playerCards2, false);
                        System.out.println("카드 정렬");
                        curBoard.printSortedCards((whoPlayer == 1) ? playerCards : playerCards2);
                        System.out.println("1 - 새로놓기, 2 - 기존패에 넣기, 3 - 카드받기 , 4 - 특정 카드 되돌리기 , 5 - 특정 카드 옮기기 , 6 - 턴 종료");
                        Scanner sc = new Scanner(System.in);
                        int input = sc.nextInt();

                        if (input == 1) {//새로 놓기
                            if (playerCards.size() <= 0)
                                continue;
                            System.out.println();
                            System.out.println("카드를 선택하세요");
                            curBoard.CheckCards((whoPlayer == 1) ? playerCards : playerCards2, true);
                            cardNew(whoPlayer, -1);

                        } else if (input == 2) {//기존패에 넣기
                            System.out.println();
                            System.out.println("카드를 선택하세요");
                            curBoard.CheckCards((whoPlayer == 1) ? playerCards : playerCards2, true);
                            cardIn(whoPlayer);


                        } else if (input == 3) {//카드 받기
                            {//초기화
                                playerCards.clear();
                                for (int i = 0; i < playerCardsTemp.size(); i++) {
                                    playerCards.add(playerCardsTemp.get(i));
                                }
                                curBoard.resetBoard();
                            }

                            System.out.println();
                            cardAdd((whoPlayer == 1) ? playerCards : playerCards2);
                            break;
                        } else if (input == 4) {//특정 카드 되돌리기
                            cardReturn(whoPlayer);
                            curBoard.CheckCards((whoPlayer == 1) ? playerCards : playerCards2, false);

                        } else if (input == 5) {//특정카드 옮기기
                            cardMove(whoPlayer);

                        } else if (input == 6) {
                            boolean result = cardCheck((whoPlayer == 1) ? player1First : player2First);

                            {//First끝난후 한개라도 배치하긴 했는지.
                                boolean result2 = false;
                                for (int i = 0; i < curBoard.boardCards.size(); i++) {
                                    for (int j = 0; j < curBoard.boardCards.get(i).size(); j++) {
                                        if (curBoard.boardCards.get(i).get(j).byPlayer) {
                                            result2 = true;
                                            break;
                                        }
                                    }
                                    if (result2)
                                        break;
                                }
                                if (result2 == false && result)
                                    result = false;
                            }

                            if (result) {//결과
                                if (whoPlayer == 1) {
                                    if (playerCards.size() == 0) {
                                        System.out.println("Player 1 승리");
                                        gameEnd = true;
                                    }
                                    player1First = true;
                                } else {
                                    if (playerCards2.size() == 0) {
                                        System.out.println("Player 2 승리");
                                        gameEnd = true;
                                    }
                                    player2First = true;
                                }

                                break;
                            } else {
                                System.out.println("턴 종료 할수없습니다");
                            }

                        }
                    }
                    System.out.println((whoPlayer == 1) ? "플레이어 1 - 카드" : "플레이어 2 - 카드");
                    curBoard.CheckCards((whoPlayer == 1) ? playerCards : playerCards2, false);

                    if (whoPlayer == 1) {//공수 교체
                        whoPlayer = 2;
                    } else {
                        whoPlayer = 1;
                    }

                }
            }

            if (gameEnd) {
                break;
            }
        }
    }

    static void gameStart2() {//AI2인플 . 이건 형진씨가 할거라 믿습니다^^7

        gameInit();//게임 초기화

        int whoPlayer = 1;//1,2. 현재 턴 누군지
        player1First = false;//30개 이상인지 체크할려고 씀
        player2First = false;
        while (true) {
            {//


                {//입력.

                    //세이브
                    ArrayList<Card> playerCardsTemp = new ArrayList<Card>();
                    {//세이브
                        for (int i = 0; i < playerCards.size(); i++) {
                            playerCardsTemp.add(playerCards.get(i));
                        }
                        curBoard.saveBoard();
                    }

                    //내가 낸건지 확인 변수 초기화
                    for (int i = 0; i < playerCards.size(); i++) {
                        playerCards.get(i).byPlayer = false;
                    }
                    for (int i = 0; i < playerCards2.size(); i++) {
                        playerCards2.get(i).byPlayer = false;
                    }
                    for (int i = 0; i < curBoard.boardCards.size(); i++) {
                        for (int j = 0; j < curBoard.boardCards.get(i).size(); j++) {
                            curBoard.boardCards.get(i).get(j).byPlayer = false;
                        }
                    }

                    while (true) {//한 턴
                        System.out.println("현재 보드");
                        curBoard.boardPrint();
                        System.out.println((whoPlayer == 1) ? "플레이어 1 - 카드" : "플레이어 2 - 카드");
                        curBoard.CheckCards((whoPlayer == 1) ? playerCards : playerCards2, false);
                        System.out.println(((whoPlayer == 1) ? playerCards : playerCards2).size());
                        System.out.println("1 - 새로놓기, 2 - 기존패에 넣기, 3 - 카드받기 , 4 - 특정 카드 되돌리기 , 5 - 특정 카드 옮기기 , 6 - 턴 종료");

                        Scanner sc = new Scanner(System.in);
                        int input = sc.nextInt();
                        if (input == 1) {//새로 놓기
                            if (playerCards.size() <= 0)
                                continue;
                            System.out.println();


                            if ((whoPlayer == 1))
                                playerCards = curBoard.sortCardsToColor(playerCards);
                            else
                                playerCards2 = curBoard.sortCardsToColor(playerCards2);

                            curBoard.CheckCards((whoPlayer == 1) ? playerCards : playerCards2, false);

                            ArrayList<Card> slicedSortedCards = new ArrayList<Card>();
                            ArrayList<Integer> slicedCardsIndex = new ArrayList<>();

                            boolean[] number = new boolean[15];

                            for (int i = 0; i <= 5; i++) {
                                for (int j = 0; j < ((whoPlayer == 1) ? playerCards : playerCards2).size(); j++) {
                                    if (((whoPlayer == 1) ? playerCards : playerCards2).get(j).sort == i && !number[((whoPlayer == 1) ? playerCards : playerCards2).get(j).cardNumber]) {
                                        if (slicedSortedCards.size() != 0) {
                                            if (((whoPlayer == 1) ? playerCards : playerCards2).get(j).cardNumber - 1 != slicedSortedCards.get(slicedSortedCards.size() - 1).cardNumber) {
                                                slicedSortedCards.clear();
                                                slicedCardsIndex.clear();
                                                number = new boolean[15];
                                            }
                                        }
                                        slicedSortedCards.add(new Card(((whoPlayer == 1) ? playerCards : playerCards2).get(j).sort, ((whoPlayer == 1) ? playerCards : playerCards2).get(j).cardNumber));
                                        slicedCardsIndex.add(j);
                                        number[((whoPlayer == 1) ? playerCards : playerCards2).get(j).cardNumber] = true;

                                        if (cardCheckCount(slicedSortedCards)) {
                                            cardNew(whoPlayer, slicedCardsIndex.get(0) + 1);

                                            for (int k = 1; k < slicedCardsIndex.size(); k++) {
                                                cardInAI(whoPlayer, slicedCardsIndex.get(k) - k + 1);
                                            }
                                        }
                                    }
                                }
                                slicedSortedCards.clear();
                                slicedCardsIndex.clear();
                                number = new boolean[15];
                            }

                            if((whoPlayer == 1))
                                playerCards = curBoard.sortCardsToNumber(playerCards);
                            else
                                playerCards2 = curBoard.sortCardsToNumber(playerCards2);

                            boolean[] color = new boolean[5];

                            for (int i = 1; i <= 13; i++) {
                                for (int j = 0; j < ((whoPlayer == 1) ? playerCards : playerCards2).size(); j++) {
                                    if (((whoPlayer == 1) ? playerCards : playerCards2).get(j).cardNumber == i && !color[((whoPlayer == 1) ? playerCards : playerCards2).get(j).sort]) {
                                        slicedSortedCards.add(new Card(((whoPlayer == 1) ? playerCards : playerCards2).get(j).sort, ((whoPlayer == 1) ? playerCards : playerCards2).get(j).cardNumber));
                                        slicedCardsIndex.add(j);
                                        color[((whoPlayer == 1) ? playerCards : playerCards2).get(j).sort] = true;
                                    }
                                }

                                if (cardCheckColor(slicedSortedCards)) {
                                    cardNew(whoPlayer, slicedCardsIndex.get(0) + 1);

                                    for (int j = 1; j < slicedCardsIndex.size(); j++)
                                        cardInAI(whoPlayer, slicedCardsIndex.get(j) - j + 1);
                                }

                                slicedSortedCards.clear();
                                slicedCardsIndex.clear();
                                color = new boolean[5];
                            }


                        } else if (input == 2) {//기존패에 넣기

                            for (int i = 0; i < curBoard.boardCards.size(); i++) {

                                if (cardCheckCount(curBoard.boardCards.get(i))) {
                                    if (curBoard.boardCards.get(i).size() < 13)
                                        for (int j = 0; j < ((whoPlayer == 1) ? playerCards : playerCards2).size(); j++) {

                                            if (checkCardToNumberInBoardForAI(((whoPlayer == 1) ? playerCards : playerCards2).get(j), curBoard.boardCards.get(i)) == 1) {
                                                cardInAIWithWay(whoPlayer, j + 1, i, 1, 1);
                                            } else if (checkCardToNumberInBoardForAI(((whoPlayer == 1) ? playerCards : playerCards2).get(j), curBoard.boardCards.get(i)) == 0) {
                                                cardInAIWithWay(whoPlayer, j + 1, i, curBoard.boardCards.get(i).size(), 0);
                                            }
                                        }
                                }

                                if (cardCheckColor(curBoard.boardCards.get(i))) {
                                    if (curBoard.boardCards.get(i).size() < 4) {
                                        for (int j = 0; j < ((whoPlayer == 1) ? playerCards : playerCards2).size(); j++) {
                                            if (((whoPlayer == 1) ? playerCards : playerCards2).get(j).sort > 3) {
                                                cardInAIWithWay(whoPlayer, j + 1, i, curBoard.boardCards.get(i).size(), 0);
                                            } else if (checkCardToColorInBoardForAI(((whoPlayer == 1) ? playerCards : playerCards2).get(j), curBoard.boardCards.get(i))) {
                                                cardInAIWithWay(whoPlayer, j + 1, i, curBoard.boardCards.get(i).size(), 0);
                                            }
                                        }
                                    }
                                }

                            }


                        } else if (input == 3) {//카드 받기
                            {//초기화
                                playerCards.clear();
                                for (int i = 0; i < playerCardsTemp.size(); i++) {
                                    playerCards.add(playerCardsTemp.get(i));
                                }
                                curBoard.resetBoard();
                            }

                            System.out.println();
                            cardAdd((whoPlayer == 1) ? playerCards : playerCards2);
                            break;

                        } else if (input == 4) {//특정 카드 되돌리기
                            //수정
                            int color = -1;
                            for(int i = 0; i < curBoard.boardCards.size(); i++) {
                                if(cardCheckCount(curBoard.boardCards.get(i))) {
                                    color = curBoard.boardCards.get(i).get(0).sort;

                                    for(int j = i+1; j < curBoard.boardCards.size(); j++) {
                                        if(cardCheckCount(curBoard.boardCards.get(j)) && curBoard.boardCards.get(j).get(0).sort == color) {
                                            if(curBoard.boardCards.get(i).get(0).cardNumber-1 == curBoard.boardCards.get(j).get(curBoard.boardCards.get(j).size()-1).cardNumber) {
                                                for(int k = 0; k < curBoard.boardCards.get(i).size(); k++) {
                                                    curBoard.addBoardCard(curBoard.boardCards.get(i).get(k), j);
                                                }
                                                curBoard.boardCards.remove(i);
                                            }
                                            else if (curBoard.boardCards.get(i).get(curBoard.boardCards.get(i).size()-1).cardNumber+1 == curBoard.boardCards.get(j).get(0).cardNumber) {
                                                for(int k = 0; k < curBoard.boardCards.get(j).size(); k++) {
                                                    curBoard.addBoardCard(curBoard.boardCards.get(j).get(k), i);
                                                }
                                                curBoard.boardCards.remove(j);
                                            }
                                        }
                                    }
                                }
                            }

                            for(int i = 0; i < curBoard.boardCards.size(); i++) {
                                if(cardCheckCount(curBoard.boardCards.get(i))) {
                                    color = curBoard.boardCards.get(i).get(0).sort;

                                    for(int j = 0; j < curBoard.boardCards.size(); j++) {
                                        if(cardCheckColor(curBoard.boardCards.get(j)) && curBoard.boardCards.get(j).size() > 3) {
                                            for(int k = 0; k < curBoard.boardCards.get(j).size(); k++) {
                                                if(curBoard.boardCards.get(j).get(k).sort == color) {
                                                    if (curBoard.boardCards.get(i).get(0).cardNumber - 1 == curBoard.boardCards.get(j).get(k).cardNumber) {
                                                        curBoard.addBoardCard2(curBoard.boardCards.get(j).get(k), i, 0);
                                                        curBoard.boardCards.get(j).remove(k);
                                                    } else if (curBoard.boardCards.get(i).get(curBoard.boardCards.get(i).size() - 1).cardNumber + 1 == curBoard.boardCards.get(j).get(k).cardNumber) {
                                                        curBoard.addBoardCard(curBoard.boardCards.get(j).get(k), i);
                                                        curBoard.boardCards.get(j).remove(k);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }




                        } else if (input == 5) {//특정카드 옮기기 또는 추가 //수정



                            ArrayList<Card> slicedTotalCards = new ArrayList<Card>();
                            ArrayList<Card> slicedPlayerCards = new ArrayList<Card>();
                            ArrayList<Card> slicedBoardCards = new ArrayList<Card>();


                            ArrayList<Integer> slicedPlayerIndex = new ArrayList<>();
                            ArrayList<Integer> slicedBoardIndex = new ArrayList<>();

                            boolean[] color = new boolean[5];
                            int boardSize = curBoard.boardCards.size();
                            for (int i = 1; i <= 13; i++) {
                                for (int j = 0; j < ((whoPlayer == 1) ? playerCards : playerCards2).size(); j++) {
                                    if (((whoPlayer == 1) ? playerCards : playerCards2).get(j).cardNumber == i && !color[((whoPlayer == 1) ? playerCards : playerCards2).get(j).sort]) {
                                        slicedTotalCards.add(new Card(((whoPlayer == 1) ? playerCards : playerCards2).get(j).sort, ((whoPlayer == 1) ? playerCards : playerCards2).get(j).cardNumber));
                                        slicedPlayerCards.add(new Card(((whoPlayer == 1) ? playerCards : playerCards2).get(j).sort, ((whoPlayer == 1) ? playerCards : playerCards2).get(j).cardNumber));
                                        slicedPlayerIndex.add(j);
                                        color[((whoPlayer == 1) ? playerCards : playerCards2).get(j).sort] = true;
                                    }
                                }

                                for (int j = 0; j < boardSize; j++) {
                                    if (cardCheckCount(curBoard.boardCards.get(j)) && !color[curBoard.boardCards.get(j).get(0).sort]) {
                                        for (int k = 0; k < curBoard.boardCards.get(j).size(); k++) {
                                            if (curBoard.boardCards.get(j).get(k).cardNumber == i) {
                                                if ((k == curBoard.boardCards.get(j).size() - 1 || k == 0) && curBoard.boardCards.get(j).size() >= 4) {
                                                    if(slicedTotalCards.size() >= 2) {
                                                        slicedTotalCards.add(new Card(curBoard.boardCards.get(j).get(k).sort, curBoard.boardCards.get(j).get(k).cardNumber));
                                                        slicedBoardCards.add(new Card(curBoard.boardCards.get(j).get(k).sort, curBoard.boardCards.get(j).get(k).cardNumber));
                                                        color[curBoard.boardCards.get(j).get(k).sort] = true;
                                                        curBoard.boardCards.get(j).remove(k);
                                                        break;
                                                    }
                                                } else if (k >= 3 && 3 < curBoard.boardCards.get(j).size() - k - 1){
                                                    slicedTotalCards.add(new Card(curBoard.boardCards.get(j).get(k).sort, curBoard.boardCards.get(j).get(k).cardNumber));
                                                    slicedBoardCards.add(new Card(curBoard.boardCards.get(j).get(k).sort, curBoard.boardCards.get(j).get(k).cardNumber));
                                                    color[curBoard.boardCards.get(j).get(k).sort] = true;

                                                    curBoard.boardCards.add(new ArrayList<Card>());

                                                    for (int q = k+1; q < curBoard.boardCards.get(j).size(); q++) {
                                                        curBoard.boardCards.get(curBoard.boardCards.size() - 1).add(new Card(curBoard.boardCards.get(j).get(q).sort, curBoard.boardCards.get(j).get(q).cardNumber));
                                                    }

                                                    for(int q = curBoard.boardCards.get(j).size()-1; q >= k ; q--) {
                                                        curBoard.boardCards.get(j).remove(q);
                                                    }

                                                    break;
                                                }
                                            }
                                        }
                                    }

                                    if (cardCheckColor(curBoard.boardCards.get(j)) && curBoard.boardCards.get(j).get(0).cardNumber == i && curBoard.boardCards.get(j).size() >= 4) {
                                        for (int k = 0; k < curBoard.boardCards.get(j).size(); k++) {
                                            if (!color[curBoard.boardCards.get(j).get(k).sort]) {
                                                slicedTotalCards.add(new Card(curBoard.boardCards.get(j).get(k).sort, curBoard.boardCards.get(j).get(k).cardNumber));
                                                slicedBoardCards.add(new Card(curBoard.boardCards.get(j).get(k).sort, curBoard.boardCards.get(j).get(k).cardNumber));
                                                color[curBoard.boardCards.get(j).get(k).sort] = true;
                                                slicedBoardIndex.add(j);
                                                curBoard.boardCards.get(j).remove(k);
                                                break;
                                            }
                                        }
                                    }
                                }

                                if (cardCheckColor(slicedTotalCards)) {
                                    cardNew(whoPlayer, slicedPlayerIndex.get(0) + 1);

                                    for (int j = 1; j < slicedPlayerIndex.size(); j++) {
                                        cardInAI(whoPlayer, slicedPlayerIndex.get(j) - j + 1);
                                    }

                                    for (int j = 0; j < slicedBoardCards.size(); j++) {
                                        curBoard.addBoardCard(slicedBoardCards.get(j), curBoard.boardCards.size() - 1);
                                    }
                                } else {
                                    for (int j = 0; j < slicedBoardIndex.size(); j++) {
                                        curBoard.boardCards.get(slicedBoardIndex.get(j)).add(new Card(slicedBoardCards.get(j).sort, slicedBoardCards.get(j).cardNumber));
                                    }
                                }

                                slicedTotalCards.clear();
                                slicedPlayerCards.clear();
                                slicedBoardCards.clear();

                                slicedPlayerIndex.clear();
                                slicedBoardIndex.clear();

                                color = new boolean[5];
                            }

                            for(int i = 0; i < ((whoPlayer == 1) ? playerCards : playerCards2).size(); i++) {
                                for(int j = 0; j < curBoard.boardCards.size(); j++) {
                                    if(cardCheckCount(curBoard.boardCards.get(j))) {
                                        if (((whoPlayer == 1) ? playerCards : playerCards2).get(i).sort == curBoard.boardCards.get(j).get(0).sort && curBoard.boardCards.get(j).size() >= 6) {
                                            for (int k = 0; k < curBoard.boardCards.get(j).size(); k++) {
                                                if(((whoPlayer == 1) ? playerCards : playerCards2).get(i).cardNumber == curBoard.boardCards.get(j).get(k).cardNumber) {
                                                    if (k >= 3 && 3 < curBoard.boardCards.get(j).size() - k) {
                                                        System.out.println(((whoPlayer == 1) ? playerCards : playerCards2).get(i).cardText+" "+ curBoard.boardCards.get(j).get(k).cardText);
                                                        curBoard.boardCards.add(new ArrayList<Card>());
                                                        for(int q = k; q < curBoard.boardCards.get(j).size(); q++) {
                                                            curBoard.addBoardCard(curBoard.boardCards.get(j).get(q), curBoard.boardCards.size()-1);
                                                        }
                                                        for(int q = curBoard.boardCards.get(j).size()-1; q > k-1; q--) {
                                                            curBoard.boardCards.get(j).remove(q);
                                                        }
                                                        cardInAIWithWay(whoPlayer, i+1,  j, k, 0);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        } else if (input == 6) {
                            boolean result = cardCheck((whoPlayer == 1) ? player1First : player2First);

                            {//First끝난후 한개라도 배치하긴 했는지.
                                boolean result2 = false;
                                for (int i = 0; i < curBoard.boardCards.size(); i++) {
                                    for (int j = 0; j < curBoard.boardCards.get(i).size(); j++) {
                                        if (curBoard.boardCards.get(i).get(j).byPlayer) {
                                            result2 = true;
                                            break;
                                        }
                                    }
                                    if (result2)
                                        break;
                                }
                                if (result2 == false && result)
                                    result = false;
                            }

                            if (result) {//결과3
                                if (whoPlayer == 1) {
                                    if (playerCards.size() == 0) {
                                        System.out.println("Player 1 승리");
                                        gameEnd = true;
                                    }
                                    player1First = true;
                                } else {
                                    if (playerCards2.size() == 0) {
                                        System.out.println("Player 2 승리");
                                        gameEnd = true;
                                    }
                                    player2First = true;
                                }

                                break;
                            } else {
                            }

                        }
                    }
                    System.out.println((whoPlayer == 1) ? "플레이어 1 - 카드" : "플레이어 2 - 카드");
                    curBoard.CheckCards((whoPlayer == 1) ? playerCards : playerCards2, false);

                    if (whoPlayer == 1) {//공수 교체
                        whoPlayer = 2;
                    } else {
                        whoPlayer = 1;
                    }
                    System.out.println();
                    System.out.println();
                    System.out.println();

                }

                if (gameEnd) {
                    System.out.println("gameEnd");
                    break;
                }

//                try {
//
//                    Thread.sleep(1000);
//
//                } catch (InterruptedException e) {
//
//                    System.out.println("error");
//
//                }
            }
        }
    }

    static void gameStart3() {//AI2인플 . 이건 형진씨가 할거라 믿습니다^^7

        gameInit();//게임 초기화

        int whoPlayer = 1;//1,2. 현재 턴 누군지
        player1First = false;//30개 이상인지 체크할려고 씀
        player2First = false;
        while (true) {
            {//


                {//입력.

                    //세이브
                    ArrayList<Card> playerCardsTemp = new ArrayList<Card>();
                    {//세이브
                        for (int i = 0; i < playerCards.size(); i++) {
                            playerCardsTemp.add(playerCards.get(i));
                        }
                        curBoard.saveBoard();
                    }

                    //내가 낸건지 확인 변수 초기화
                    for (int i = 0; i < playerCards.size(); i++) {
                        playerCards.get(i).byPlayer = false;
                    }
                    for (int i = 0; i < playerCards2.size(); i++) {
                        playerCards2.get(i).byPlayer = false;
                    }
                    for (int i = 0; i < curBoard.boardCards.size(); i++) {
                        for (int j = 0; j < curBoard.boardCards.get(i).size(); j++) {
                            curBoard.boardCards.get(i).get(j).byPlayer = false;
                        }
                    }

                    while (true) {//한 턴
                        System.out.println("현재 보드");
                        curBoard.boardPrint();
                        System.out.println((whoPlayer == 1) ? "플레이어 1 - 카드" : "플레이어 2 - 카드");
                        curBoard.CheckCards((whoPlayer == 1) ? playerCards : playerCards2, false);
                        System.out.println(((whoPlayer == 1) ? playerCards : playerCards2).size());
//                        System.out.println("1 - 새로놓기, 2 - 기존패에 넣기, 3 - 카드받기 , 4 - 특정 카드 되돌리기 , 5 - 특정 카드 옮기기 , 6 - 턴 종료");

//                        Scanner sc = new Scanner(System.in);
//                        int input = sc.nextInt();
//                        if (input == 1) {//새로 놓기
                            if (playerCards.size() <= 0)
                                continue;
                            System.out.println();


                            if ((whoPlayer == 1))
                                playerCards = curBoard.sortCardsToColor(playerCards);
                            else
                                playerCards2 = curBoard.sortCardsToColor(playerCards2);

                            curBoard.CheckCards((whoPlayer == 1) ? playerCards : playerCards2, false);

                            ArrayList<Card> slicedSortedCards = new ArrayList<Card>();
                            ArrayList<Integer> slicedCardsIndex = new ArrayList<>();

                            boolean[] number = new boolean[15];

                            for (int i = 0; i <= 5; i++) {
                                for (int j = 0; j < ((whoPlayer == 1) ? playerCards : playerCards2).size(); j++) {
                                    if (((whoPlayer == 1) ? playerCards : playerCards2).get(j).sort == i && !number[((whoPlayer == 1) ? playerCards : playerCards2).get(j).cardNumber]) {
                                        if (slicedSortedCards.size() != 0) {
                                            if (((whoPlayer == 1) ? playerCards : playerCards2).get(j).cardNumber - 1 != slicedSortedCards.get(slicedSortedCards.size() - 1).cardNumber) {
                                                slicedSortedCards.clear();
                                                slicedCardsIndex.clear();
                                                number = new boolean[15];
                                            }
                                        }
                                        slicedSortedCards.add(new Card(((whoPlayer == 1) ? playerCards : playerCards2).get(j).sort, ((whoPlayer == 1) ? playerCards : playerCards2).get(j).cardNumber));
                                        slicedCardsIndex.add(j);
                                        number[((whoPlayer == 1) ? playerCards : playerCards2).get(j).cardNumber] = true;

                                        if (cardCheckCount(slicedSortedCards)) {
                                            cardNew(whoPlayer, slicedCardsIndex.get(0) + 1);

                                            for (int k = 1; k < slicedCardsIndex.size(); k++) {
                                                cardInAI(whoPlayer, slicedCardsIndex.get(k) - k + 1);
                                            }
                                        }
                                    }
                                }
                                slicedSortedCards.clear();
                                slicedCardsIndex.clear();
                                number = new boolean[15];
                            }

                            if((whoPlayer == 1))
                                playerCards = curBoard.sortCardsToNumber(playerCards);
                            else
                                playerCards2 = curBoard.sortCardsToNumber(playerCards2);

                            boolean[] color = new boolean[5];

                            for (int i = 1; i <= 13; i++) {
                                for (int j = 0; j < ((whoPlayer == 1) ? playerCards : playerCards2).size(); j++) {
                                    if (((whoPlayer == 1) ? playerCards : playerCards2).get(j).cardNumber == i && !color[((whoPlayer == 1) ? playerCards : playerCards2).get(j).sort]) {
                                        slicedSortedCards.add(new Card(((whoPlayer == 1) ? playerCards : playerCards2).get(j).sort, ((whoPlayer == 1) ? playerCards : playerCards2).get(j).cardNumber));
                                        slicedCardsIndex.add(j);
                                        color[((whoPlayer == 1) ? playerCards : playerCards2).get(j).sort] = true;
                                    }
                                }

                                if (cardCheckColor(slicedSortedCards)) {
                                    cardNew(whoPlayer, slicedCardsIndex.get(0) + 1);

                                    for (int j = 1; j < slicedCardsIndex.size(); j++)
                                        cardInAI(whoPlayer, slicedCardsIndex.get(j) - j + 1);
                                }

                                slicedSortedCards.clear();
                                slicedCardsIndex.clear();
                                color = new boolean[5];
                            }


//                        } else if (input == 2) {//기존패에 넣기

                            for (int i = 0; i < curBoard.boardCards.size(); i++) {

                                if (cardCheckCount(curBoard.boardCards.get(i))) {
                                    if (curBoard.boardCards.get(i).size() < 13)
                                        for (int j = 0; j < ((whoPlayer == 1) ? playerCards : playerCards2).size(); j++) {

                                            if (checkCardToNumberInBoardForAI(((whoPlayer == 1) ? playerCards : playerCards2).get(j), curBoard.boardCards.get(i)) == 1) {
                                                cardInAIWithWay(whoPlayer, j + 1, i, 1, 1);
                                            } else if (checkCardToNumberInBoardForAI(((whoPlayer == 1) ? playerCards : playerCards2).get(j), curBoard.boardCards.get(i)) == 0) {
                                                cardInAIWithWay(whoPlayer, j + 1, i, curBoard.boardCards.get(i).size(), 0);
                                            }
                                        }
                                }

                                if (cardCheckColor(curBoard.boardCards.get(i))) {
                                    if (curBoard.boardCards.get(i).size() < 4)
                                        for (int j = 0; j < ((whoPlayer == 1) ? playerCards : playerCards2).size(); j++) {
                                            if (((whoPlayer == 1) ? playerCards : playerCards2).get(j).sort > 3) {
                                                cardInAIWithWay(whoPlayer, j + 1, i, curBoard.boardCards.get(i).size(), 0);
                                            } else if (checkCardToColorInBoardForAI(((whoPlayer == 1) ? playerCards : playerCards2).get(j), curBoard.boardCards.get(i))) {
                                                cardInAIWithWay(whoPlayer, j + 1, i, curBoard.boardCards.get(i).size(), 0);
                                            }
                                        }
                                }

                            }


//                        } else if (input == 3) {//카드 받기


//                        } else if (input == 4) {//특정 카드 되돌리기
                            //수정


//                        } else if (input == 5) {//특정카드 옮기기 //수정



                            ArrayList<Card> slicedTotalCards = new ArrayList<Card>();
                            ArrayList<Card> slicedPlayerCards = new ArrayList<Card>();
                            ArrayList<Card> slicedBoardCards = new ArrayList<Card>();


                            ArrayList<Integer> slicedPlayerIndex = new ArrayList<>();
                            ArrayList<Integer> slicedBoardIndex = new ArrayList<>();

                            color = new boolean[5];
                            int boardSize = curBoard.boardCards.size();
                            for (int i = 1; i <= 13; i++) {
                                for (int j = 0; j < ((whoPlayer == 1) ? playerCards : playerCards2).size(); j++) {
                                    if (((whoPlayer == 1) ? playerCards : playerCards2).get(j).cardNumber == i && !color[((whoPlayer == 1) ? playerCards : playerCards2).get(j).sort]) {
                                        slicedTotalCards.add(new Card(((whoPlayer == 1) ? playerCards : playerCards2).get(j).sort, ((whoPlayer == 1) ? playerCards : playerCards2).get(j).cardNumber));
                                        slicedPlayerCards.add(new Card(((whoPlayer == 1) ? playerCards : playerCards2).get(j).sort, ((whoPlayer == 1) ? playerCards : playerCards2).get(j).cardNumber));
                                        slicedPlayerIndex.add(j);
                                        color[((whoPlayer == 1) ? playerCards : playerCards2).get(j).sort] = true;
                                    }
                                }


                                for (int j = 0; j < boardSize; j++) {
                                    if (cardCheckCount(curBoard.boardCards.get(j)) && !color[curBoard.boardCards.get(j).get(0).sort]) {
                                        for (int k = 0; k < curBoard.boardCards.get(j).size(); k++) {
                                            if (curBoard.boardCards.get(j).get(k).cardNumber == i) {
                                                if ((k == curBoard.boardCards.get(j).size() - 1 || k == 0) && curBoard.boardCards.get(j).size() >= 4) {
                                                    if(slicedTotalCards.size() >= 2) {
                                                        slicedTotalCards.add(new Card(curBoard.boardCards.get(j).get(k).sort, curBoard.boardCards.get(j).get(k).cardNumber));
                                                        slicedBoardCards.add(new Card(curBoard.boardCards.get(j).get(k).sort, curBoard.boardCards.get(j).get(k).cardNumber));
                                                        color[curBoard.boardCards.get(j).get(k).sort] = true;
                                                        curBoard.boardCards.get(j).remove(k);
                                                        break;
                                                    }
                                                } else if (k > 3 && 3 < curBoard.boardCards.get(j).size() - k - 1){
                                                    slicedTotalCards.add(new Card(curBoard.boardCards.get(j).get(k).sort, curBoard.boardCards.get(j).get(k).cardNumber));
                                                    slicedBoardCards.add(new Card(curBoard.boardCards.get(j).get(k).sort, curBoard.boardCards.get(j).get(k).cardNumber));
                                                    color[curBoard.boardCards.get(j).get(k).sort] = true;

                                                    curBoard.boardCards.add(new ArrayList<Card>());

                                                    for (int q = k+1; q < curBoard.boardCards.get(j).size(); q++) {
                                                        curBoard.boardCards.get(curBoard.boardCards.size() - 1).add(new Card(curBoard.boardCards.get(j).get(q).sort, curBoard.boardCards.get(j).get(q).cardNumber));
                                                    }

                                                    for(int q = curBoard.boardCards.get(j).size()-1; q >= k ; q--) {
                                                        curBoard.boardCards.get(j).remove(q);
                                                    }

                                                    break;
                                                }
                                            }
                                        }
                                    }

                                    if (cardCheckColor(curBoard.boardCards.get(j)) && curBoard.boardCards.get(j).get(0).cardNumber == i && curBoard.boardCards.get(j).size() >= 4) {
                                        for (int k = 0; k < curBoard.boardCards.get(j).size(); k++) {
                                            if (!color[curBoard.boardCards.get(j).get(k).sort]) {
                                                slicedTotalCards.add(new Card(curBoard.boardCards.get(j).get(k).sort, curBoard.boardCards.get(j).get(k).cardNumber));
                                                slicedBoardCards.add(new Card(curBoard.boardCards.get(j).get(k).sort, curBoard.boardCards.get(j).get(k).cardNumber));
                                                color[curBoard.boardCards.get(j).get(k).sort] = true;
                                                slicedBoardIndex.add(j);
                                                curBoard.boardCards.get(j).remove(k);
                                                break;
                                            }
                                        }
                                    }
                                }

                                if (cardCheckColor(slicedTotalCards)) {
                                    cardNew(whoPlayer, slicedPlayerIndex.get(0) + 1);

                                    for (int j = 1; j < slicedPlayerIndex.size(); j++) {
                                        cardInAI(whoPlayer, slicedPlayerIndex.get(j) - j + 1);
                                    }

                                    for (int j = 0; j < slicedBoardCards.size(); j++) {
                                        curBoard.addBoardCard(slicedBoardCards.get(j), curBoard.boardCards.size() - 1);
                                    }
                                } else {
                                    for (int j = 0; j < slicedBoardIndex.size(); j++) {
                                        curBoard.boardCards.get(slicedBoardIndex.get(j)).add(new Card(slicedBoardCards.get(j).sort, slicedBoardCards.get(j).cardNumber));
                                    }
                                }

                                slicedTotalCards.clear();
                                slicedPlayerCards.clear();
                                slicedBoardCards.clear();

                                slicedPlayerIndex.clear();
                                slicedBoardIndex.clear();

                                color = new boolean[5];
                            }

                        for(int i = 0; i < ((whoPlayer == 1) ? playerCards : playerCards2).size(); i++) {
                            for(int j = 0; j < curBoard.boardCards.size(); j++) {
                                if(cardCheckCount(curBoard.boardCards.get(j))) {
                                    if (((whoPlayer == 1) ? playerCards : playerCards2).get(i).sort == curBoard.boardCards.get(j).get(0).sort && curBoard.boardCards.get(j).size() >= 6) {
                                        for (int k = 0; k < curBoard.boardCards.get(j).size(); k++) {
                                            if(((whoPlayer == 1) ? playerCards : playerCards2).get(i).cardNumber == curBoard.boardCards.get(j).get(k).cardNumber) {
                                                if (k >= 3 && 3 < curBoard.boardCards.get(j).size() - k) {
                                                    curBoard.boardCards.add(new ArrayList<Card>());
                                                    for(int q = k; q < curBoard.boardCards.get(j).size(); q++) {
                                                        curBoard.addBoardCard(curBoard.boardCards.get(j).get(q), curBoard.boardCards.size()-1);
                                                    }
                                                    for(int q = curBoard.boardCards.get(j).size()-1; q > k-1; q--) {
                                                        curBoard.boardCards.get(j).remove(q);
                                                    }
                                                    cardInAIWithWay(whoPlayer, i+1,  j, k, 0);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

//                        } else if (input == 6) {
                            boolean result = cardCheck((whoPlayer == 1) ? player1First : player2First);

                            {//First끝난후 한개라도 배치하긴 했는지.
                                boolean result2 = false;
                                for (int i = 0; i < curBoard.boardCards.size(); i++) {
                                    for (int j = 0; j < curBoard.boardCards.get(i).size(); j++) {
                                        if (curBoard.boardCards.get(i).get(j).byPlayer) {
                                            result2 = true;
                                            break;
                                        }
                                    }
                                    if (result2)
                                        break;
                                }
                                if (result2 == false && result)
                                    result = false;
                            }

                            if (result) {//결과3
                                if (whoPlayer == 1) {
                                    if (playerCards.size() == 0) {
                                        System.out.println("Player 1 승리");
                                        gameEnd = true;
                                    }
                                    player1First = true;
                                } else {
                                    if (playerCards2.size() == 0) {
                                        System.out.println("Player 2 승리");
                                        gameEnd = true;
                                    }
                                    player2First = true;
                                }

                                System.out.println("현재 보드");
                                curBoard.boardPrint();

                                break;
                            } else {
                                int nowColor = -1;
                                for(int i = 0; i < curBoard.boardCards.size(); i++) {
                                    if(cardCheckCount(curBoard.boardCards.get(i))) {
                                        nowColor = curBoard.boardCards.get(i).get(0).sort;

                                        for(int j = i+1; j < curBoard.boardCards.size(); j++) {
                                            if(cardCheckCount(curBoard.boardCards.get(j)) && curBoard.boardCards.get(j).get(0).sort == nowColor) {
                                                if(curBoard.boardCards.get(i).get(0).cardNumber-1 == curBoard.boardCards.get(j).get(curBoard.boardCards.get(j).size()-1).cardNumber) {
                                                    for(int k = 0; k < curBoard.boardCards.get(i).size(); k++) {
                                                        curBoard.addBoardCard(curBoard.boardCards.get(i).get(k), j);
                                                    }
                                                    curBoard.boardCards.remove(i);
                                                }
                                                else if (curBoard.boardCards.get(i).get(curBoard.boardCards.get(i).size()-1).cardNumber+1 == curBoard.boardCards.get(j).get(0).cardNumber) {
                                                    for(int k = 0; k < curBoard.boardCards.get(j).size(); k++) {
                                                        curBoard.addBoardCard(curBoard.boardCards.get(j).get(k), i);
                                                    }
                                                    curBoard.boardCards.remove(j);
                                                }
                                            }
                                        }
                                    }
                                }

                                for(int i = 0; i < curBoard.boardCards.size(); i++) {
                                    if(cardCheckCount(curBoard.boardCards.get(i))) {
                                        nowColor = curBoard.boardCards.get(i).get(0).sort;

                                        for(int j = 0; j < curBoard.boardCards.size(); j++) {
                                            if(cardCheckColor(curBoard.boardCards.get(j)) && curBoard.boardCards.get(j).size() > 3) {
                                                for(int k = 0; k < curBoard.boardCards.get(j).size(); k++) {
                                                    if(curBoard.boardCards.get(j).get(k).sort == nowColor) {
                                                        if (curBoard.boardCards.get(i).get(0).cardNumber - 1 == curBoard.boardCards.get(j).get(k).cardNumber) {
                                                            curBoard.addBoardCard2(curBoard.boardCards.get(j).get(k), i, 0);
                                                            curBoard.boardCards.get(j).remove(k);
                                                        } else if (curBoard.boardCards.get(i).get(curBoard.boardCards.get(i).size() - 1).cardNumber + 1 == curBoard.boardCards.get(j).get(k).cardNumber) {
                                                            curBoard.addBoardCard(curBoard.boardCards.get(j).get(k), i);
                                                            curBoard.boardCards.get(j).remove(k);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            {//초기화
                                playerCards.clear();
                                for (int i = 0; i < playerCardsTemp.size(); i++) {
                                    playerCards.add(playerCardsTemp.get(i));
                                }
                                curBoard.resetBoard();
                            }

                            System.out.println();
                            cardAdd((whoPlayer == 1) ? playerCards : playerCards2);
                            break;

//                        }
                    }
                    System.out.println((whoPlayer == 1) ? "플레이어 1 - 카드" : "플레이어 2 - 카드");
                    curBoard.CheckCards((whoPlayer == 1) ? playerCards : playerCards2, false);

                    if (whoPlayer == 1) {//공수 교체
                        whoPlayer = 2;
                    } else {
                        whoPlayer = 1;
                    }
//                    System.out.println();
//                    System.out.println();
//                    System.out.println();

                }

                if (gameEnd) {
                    System.out.println("gameEnd");
                    break;
                }

                try {

                    Thread.sleep(200);

                } catch (InterruptedException e) {

                    System.out.println("error");

                }
            }
        }
    }

    static int cardSelect(int cardSize) {//카드선택 방향키 이동
        System.out.println("1~" + cardSize + "숫자 선택 입력");
        Scanner sc = new Scanner(System.in);
        int cardSelection = -1;
        while (true) {
            int input = sc.nextInt();
            if (input <= 0) {
                continue;
            } else if (input > cardSize) {
                continue;
            } else {
                cardSelection = input;
                break;
            }
        }
        return cardSelection;
    }

    static void cardAdd(ArrayList<Card> card) {
        if (Cards.size() == 0)
            return;
//        if(card.size()==20)
//            return;
        int drawIndex = (int) (Math.random() * Cards.size());
        card.add(Cards.get(drawIndex));
        Cards.remove(drawIndex);
    }

    static void cardNew(int whoPlayer, int select) {//카드 새로넣기
        if (select == -1)
            select = cardSelect((whoPlayer == 1) ? playerCards.size() : playerCards2.size());//카드 선택

        curBoard.boardCards.add(new ArrayList<Card>());
        curBoard.addBoardCard((whoPlayer == 1) ? playerCards.get(select - 1) :
                playerCards2.get(select - 1), curBoard.boardCards.size() - 1);

        if (whoPlayer == 1) {
            playerCards.get(select - 1).byPlayer = true;
            playerCards.remove(select - 1);
        } else {
            playerCards2.get(select - 1).byPlayer = true;
            playerCards2.remove(select - 1);
        }
    }


    static void cardIn(int whoPlayer) {//카드 기존꺼에 넣기
        int select = cardSelect((whoPlayer == 1) ? playerCards.size() : playerCards2.size());//카드 선택
        Scanner sc = new Scanner(System.in);
        int selection = 0;
        {//배열 선택
            curBoard.boardPrint();
            while (true) {
                System.out.println("몇번째 그룹에 넣으시겠습니까요");
                int input = sc.nextInt();
                if (!(input <= 0 || input > curBoard.boardCards.size())) {
                    selection = input;
                    break;
                } else {
                    continue;
                }
            }
        }

        int selection2 = 0;
        {//배열 요소 선택
            while (true) {
                curBoard.boardElementPrint(selection);
                System.out.println("몇번째 요소를 선택하세요");
                int input = sc.nextInt();
                if (!(input <= 0 || input > curBoard.boardCards.get(selection - 1).size())) {
                    selection2 = input;
                    break;
                } else {
                    continue;
                }
            }
        }

        if ((whoPlayer == 1) ? player1First : player2First) {
            for (int i = 0; i < curBoard.boardCards.get(selection - 1).size(); i++) {
//                if (curBoard.boardCards.get(selection - 1).get(i).byPlayer == false) {
//                    System.out.println("처음엔 다른 카드와 조합불가능");
//                    return;
//                }
            }
        }


        while (true) {
            System.out.println("1- 오른쪽 , 2 - 왼쪽");
            int input = sc.nextInt();
            if (input == 1) {
                curBoard.addBoardCard2((whoPlayer == 1) ? playerCards.get(select - 1) :
                        playerCards2.get(select - 1), selection - 1, selection2);
                if (whoPlayer == 1) {
                    playerCards.get(select - 1).byPlayer = true;
                    playerCards.remove(select - 1);
                } else {
                    playerCards2.get(select - 1).byPlayer = true;
                    playerCards2.remove(select - 1);
                }
                break;
            } else if (input == 2) {
                curBoard.addBoardCard2((whoPlayer == 1) ? playerCards.get(select - 1) :
                        playerCards2.get(select - 1), selection - 1, selection2 - 1);
                if (whoPlayer == 1) {
                    playerCards.get(select - 1).byPlayer = true;
                    playerCards.remove(select - 1);
                } else {
                    playerCards2.get(select - 1).byPlayer = true;
                    playerCards2.remove(select - 1);
                }
                break;
            }
        }
    }

    static void cardInAI(int whoPlayer, int select) {//카드 기존꺼에 넣기
        int toGroup = curBoard.boardCards.size() - 1;
        int toIndex = curBoard.boardCards.get(toGroup).size();

        curBoard.addBoardCard2((whoPlayer == 1) ? playerCards.get(select - 1) :
                playerCards2.get(select - 1), toGroup, toIndex);

        if (whoPlayer == 1) {
            playerCards.get(select - 1).byPlayer = true;
            playerCards.remove(select - 1);
        } else {
            playerCards2.get(select - 1).byPlayer = true;
            playerCards2.remove(select - 1);
        }
    }

    static void cardInAIWithWay(int whoPlayer, int select, int toGroup, int toIndex, int way) {
        curBoard.addBoardCard2((whoPlayer == 1) ? playerCards.get(select - 1) :
                playerCards2.get(select - 1), toGroup, toIndex - way);

        if (whoPlayer == 1) {
            playerCards.get(select - 1).byPlayer = true;
            playerCards.remove(select - 1);
        } else {
            playerCards2.get(select - 1).byPlayer = true;
            playerCards2.remove(select - 1);
        }
    }


    static boolean checkCardToColorInBoardForAI(Card card, ArrayList<Card> board) {
        for(int i = 0; i < board.size(); i++) {
            if(board.get(i).sort > 3 ) {}
            else if(card.cardNumber == board.get(i).cardNumber && card.sort != board.get(i).sort) {}
            else return false;
        }
        return true;
    }

    static int checkCardToNumberInBoardForAI(Card card, ArrayList<Card> board) {
        if(card.cardNumber-1==board.get(board.size()-1).cardNumber && card.sort==board.get(0).sort) return 0; // 뒤에
        else if(card.cardNumber+1==board.get(0).cardNumber && card.sort==board.get(0).sort) return 1; //앞에
        else return -1;
    }

    static void cardReturn(int whoPlayer) {//특정카드 되돌리기
        Scanner sc=new Scanner(System.in);
        int selection=0;
        {//배열 선택
            curBoard.boardPrint();
            while(true) {
                System.out.println("몇번째 그룹에서 빼시겠습니까요");
                int input=sc.nextInt();
                if( !(input<=0 || input>curBoard.boardCards.size()) ) {
                    selection=input;
                    break;
                }else {
                    continue;
                }
            }
        }

        int selection2=0;
        {//배열 요소 선택
            while(true) {
                curBoard.boardElementPrint(selection);
                System.out.println("몇번째 요소를 선택하세요");
                int input=sc.nextInt();
                if( !(input<=0 || input>curBoard.boardCards.get(selection-1).size()) ) {
                    selection2=input;
                    break;
                }else {
                    continue;
                }
            }
        }


        if(curBoard.boardCards.get(selection-1).get(selection2-1).byPlayer) {
            if(whoPlayer==1) {
                curBoard.boardCards.get(selection-1).get(selection2-1).byPlayer=false;
                playerCards.add(curBoard.boardCards.get(selection-1).get(selection2-1));
            }else{
                curBoard.boardCards.get(selection-1).get(selection2-1).byPlayer=false;
                playerCards2.add(curBoard.boardCards.get(selection-1).get(selection2-1));
            }

            curBoard.boardCards.get(selection-1).remove(selection2-1);
            if(curBoard.boardCards.get(selection-1).size()==0) {//보드 배열삭제
                curBoard.boardCards.remove(selection-1);
            }
        }
    }

    static void cardMove(int whoPlayer) {
        Scanner sc=new Scanner(System.in);
        int selection=0;
        {//배열 선택
            curBoard.boardPrint();
            while(true) {
                System.out.println("몇번째 그룹에서 빼시겠습니까요");
                int input=sc.nextInt();
                if( !(input<=0 || input>curBoard.boardCards.size()) ) {
                    selection=input;
                    break;
                }else {
                    continue;
                }
            }
        }

        int selection2=0;
        {//배열 요소 선택
            while(true) {
                curBoard.boardElementPrint(selection);
                System.out.println("몇번째 요소를 선택하세요");
                int input=sc.nextInt();
                if( !(input<=0 || input>curBoard.boardCards.get(selection-1).size()) ) {
                    selection2=input;
                    break;
                }else {
                    continue;
                }
            }
        }

        System.out.println();
        System.out.println("어디로 옮기시겠습니까");

        int selection3=0;
        {//배열 선택
            curBoard.boardPrint();
            while(true) {
                System.out.println("몇번째 그룹에서 빼시겠습니까요");
                int input=sc.nextInt();
                if( !(input<=0 || input>curBoard.boardCards.size()) ) {
                    selection3=input;
                    break;
                }else {
                    continue;
                }
            }
        }

        int selection4=0;
        {//배열 요소 선택
            while(true) {
                curBoard.boardElementPrint(selection3);
                System.out.println("몇번째 요소를 선택하세요");
                int input=sc.nextInt();
                if( !(input<=0 || input>curBoard.boardCards.get(selection3-1).size()) ) {
                    selection4=input;
                    break;
                }else {
                    continue;
                }
            }
        }

        if((whoPlayer==1)?player1First:player2First) {
            for(int i=0;i<curBoard.boardCards.get(selection3-1).size();i++) {
//                if(curBoard.boardCards.get(selection3-1).get(i).byPlayer==false) {
//                    System.out.println("처음엔 다른 카드와 조합불가능");
//                    return;
//                }
            }
        }

        int input;
        while(true) {
            System.out.println("1- 오른쪽 , 2 - 왼쪽");
            input=sc.nextInt();
            if(input==1) {
                curBoard.addBoardCard2(curBoard.boardCards.get(selection-1).get(selection2-1), selection3-1, selection4);
                break;
            }else if(input==2) {
                curBoard.addBoardCard2(curBoard.boardCards.get(selection-1).get(selection2-1), selection3-1, selection4-1);
                break;
            }
        }

        curBoard.boardCards.get(selection-1).remove(selection2-1);
        if(curBoard.boardCards.get(selection-1).size()==0) {//보드 배열삭제
            curBoard.boardCards.remove(selection-1);
        }

    }

    static int count=0;
    static boolean cardCheck(boolean isFirst) {
        //같은숫자의 다른 컬러
        //같은 컬러의 순서대로 숫자
        //조커는 어디 가든 상관없음
        //첨엔 총합 30
        count=0;
        for(int i=0;i<curBoard.boardCards.size();i++) {
            boolean result=true;
            boolean tempResult=true;
            tempResult=cardCheckColor(curBoard.boardCards.get(i));
            result&=tempResult;
            if(tempResult)
                continue;
            tempResult=cardCheckCount(curBoard.boardCards.get(i));
            result&=tempResult;
            if(tempResult)
                continue;
            if(result==false) {
                return false;
            }
        }
        if(isFirst==false) {
            if(count<30) {
                //System.out.println("총합이 30이상이 되어야합니다.");
                return false;
            }
        }
        return true;
    }

    static boolean cardCheckColor(ArrayList<Card> card) {//넘버는 같고 색은 다른놈들
        if(card.size()<3)
            return false;

        int prevCount=card.get(0).cardNumber;
        ArrayList<Boolean> cardColors=new ArrayList<Boolean>();
        for(int i=0;i<4;i++) {//순서대로 검정,빨강,노랑,파랑
            cardColors.add(false);
        }


        int tempCount=0;
        tempCount+=prevCount;
        for(int i=0;i<card.size();i++) {//검증
            if(prevCount==0) {
                prevCount=card.get(i).cardNumber;
            }
            if(prevCount==card.get(i).cardNumber) {
                if(card.get(i).sort>3) {
                    //조커
                }
                else if(cardColors.get(card.get(i).sort)) {
                    //System.out.println("색이 두개이상이면 안됩니다");
                    return false;
                }else {
                    cardColors.set(card.get(i).sort, true);
                }
            }else if(card.get(i).cardNumber!=0){
                //System.out.println("번호가 다릅니다"+prevCount+":"+card.get(i).cardNumber);
                return false;
            }
            tempCount+=card.get(i).cardNumber;
        }
        count+=tempCount;

        return true;
    }

    static boolean cardCheckCount(ArrayList<Card> card) {//넘버는 순서대로, 색은 같은놈들
        if(card.size()<3)
            return false;

        int tempCount=0;
        int prevColor=card.get(0).sort;
        int prevCount=card.get(0).cardNumber;
        tempCount+=prevCount;


        for(int i=1;i<card.size();i++) {
            if(prevCount==0) {
                prevCount=card.get(i).cardNumber;
                prevColor=card.get(i).sort;
            }
            if(prevColor==card.get(i).sort) { //색 같냐
                if(prevCount!=card.get(i).cardNumber-1 && card.get(i).cardNumber!=0) {
                    //System.out.println("번호가 정렬되지않았습니다"+prevCount+":"+(card.get(i).cardNumber-1));
                    return false;
                }
            }else if(card.get(i).cardNumber!=0) {
                //System.out.println("색이 같지않습니다");
                return false;
            }
            if(card.get(i).cardNumber!=0) {
                prevCount=card.get(i).cardNumber;
            }
            tempCount+=card.get(i).cardNumber;
        }
        //System.out.println(tempCount+"C");
        count+=tempCount;
        return true;
    }

}



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



class Board{//카드를 배치하였을때 반영하고 그런 ui역할, 현재 내 카드 확인


    public ArrayList<ArrayList<Card>> boardCards=new ArrayList<ArrayList<Card>>();

    public void CheckCards(ArrayList<Card> card,boolean useIndex) {//현재 내 카드들 출력
        for(int i=0;i<card.size();i++) {
            Card temp=card.get(i);
            System.out.print((useIndex)?(i+1)+":"+card.get(i).cardText:""+card.get(i).cardText);
        }
        System.out.println();
    }

    public void printSortedCards(ArrayList<Card> card) {
        ArrayList<Card> toNumber = sortCardsToNumber(card);
        ArrayList<Card> toColor = sortCardsToColor(card);

        for(int i = 0; i < toNumber.size(); i++)
            System.out.print(toNumber.get(i).cardText);
        System.out.println();

        for(int i = 0; i < toColor.size(); i++)
            System.out.print(toColor.get(i).cardText);
        System.out.println();
    }

    public ArrayList sortCardsToNumber(ArrayList<Card> card) { // 숫자 정렬
        ArrayList<Card> sortedResult = new ArrayList<Card>();
        ArrayList<Card> sortedNumber = new ArrayList<Card>();

        for(int i = 0; i <= 13; i++) {
            for(int j = 0; j < card.size(); j++)
                if (card.get(j).cardNumber == i)
                    sortedNumber.add(new Card(card.get(j).sort, card.get(j).cardNumber));

            for(int j = 0; j <= 5; j++)
                for(int k = 0; k < sortedNumber.size(); k++)
                    if(sortedNumber.get(k).sort==j)
                        sortedResult.add(new Card(sortedNumber.get(k).sort, sortedNumber.get(k).cardNumber));

            sortedNumber.clear();
        }

        return sortedResult;
    }

    public ArrayList sortCardsToColor(ArrayList<Card> card) { // 색깔 정렬
        ArrayList<Card> sortedResult = new ArrayList<Card>();
        ArrayList<Card> sortedNumber = new ArrayList<Card>();

        for(int i = 0; i <= 5; i++) {
            for (int j = 0; j < card.size(); j++)
                if (card.get(j).sort == i)
                    sortedNumber.add(new Card(card.get(j).sort, card.get(j).cardNumber));

            for(int j = 0; j <= 13; j++)
                for(int k = 0; k < sortedNumber.size(); k++)
                    if(sortedNumber.get(k).cardNumber==j)
                        sortedResult.add(new Card(sortedNumber.get(k).sort, sortedNumber.get(k).cardNumber));

            sortedNumber.clear();
        }

        return sortedResult;
    }

    public void boardPrint() {//현재 보드 출력
        for(int i=0;i<boardCards.size();i++) {
            System.out.print((i+1)+":");
            for(int j=0;j<boardCards.get(i).size();j++) {
                System.out.print(boardCards.get(i).get(j).cardText);
            }
            System.out.println();
        }
    }

    public void addBoardCard(Card card,int index) {//새로넣기 할때 카드 넣는거임
        boardCards.get(index).add(card);
    }

    public void addBoardCard2(Card card,int index,int index2) {//지정해서 카드 넣는거임
        boardCards.get(index).add(index2, card);
    }

    public void resetBoard() {//카드받기하면 초기화해주는거
        boardCards.clear();
        for(int i=0;i<boardCardsTemp.size();i++) {
            boardCards.add(boardCardsTemp.get(i));
        }
    }

    static ArrayList<ArrayList<Card>> boardCardsTemp=new ArrayList<ArrayList<Card>>();
    public void saveBoard() {
        boardCardsTemp.clear();
        for(int i=0;i<boardCards.size();i++) {
            boardCardsTemp.add(boardCards.get(i));
        }
    }

    public void boardElementPrint(int index) {//특정 보드 배열 출력
        for(int i=0;i<boardCards.get(index-1).size();i++) {
            System.out.print((i+1)+":");
            System.out.print(boardCards.get(index-1).get(i).cardText);
            System.out.println();
        }
    }
}