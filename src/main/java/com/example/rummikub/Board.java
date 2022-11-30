package com.example.rummikub;

import java.util.ArrayList;

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
