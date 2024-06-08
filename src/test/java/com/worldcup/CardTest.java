package com.worldcup;

import static org.junit.Assert.*;

import org.junit.Test;

public class CardTest {

    public Card card;

    @Test
    public void testCard() {
        String card = Card.YELLOW.getCard("YELLOW");
        String expected = "YELLOW";
        assertEquals(expected, card);
    }

    @Test
    public void testCard2() {
        String card = Card.RED.getCard("RED");
        String expected = "RED";
        assertEquals(expected, card);
    }

    @Test
    public void testRedCardLeadsToPlayerEjection() { // Kiểm tra thẻ đỏ thì đuổi khỏi sân
        boolean expected = true;
        Player player = new Player("Test Player");
        player.receiveCard(Card.RED);
        assertEquals(expected, player.isEjected());
    }

    @Test
    public void testTwoYellowCardsLeadToRedCard() { // kiểm tra cầu thủ bị 2 thẻ vàng = 1 thẻ đỏ và bị đuổi khỏi sân
        Player player = new Player("Test Player");
        player.receiveCard(Card.YELLOW);
        player.receiveCard(Card.YELLOW);
        player.receiveCard(Card.RED);
        assertTrue(player.isEjected());
    }

}
