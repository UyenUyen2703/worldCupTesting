package com.worldcup;

public class Player {
    public String name;
    public boolean ejected;

    public Player(String name) {
        this.name = name;
        this.ejected = false;
    }

    public void receiveCard(Card card) {
        if (card == Card.RED || (card == Card.YELLOW && this.ejected)) {
            this.ejected = true;
        }
    }

    public boolean isEjected() {
        return ejected;
    }
}

