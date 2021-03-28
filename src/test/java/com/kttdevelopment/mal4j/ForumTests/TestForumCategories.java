package com.kttdevelopment.mal4j.ForumTests;

import com.kttdevelopment.mal4j.MyAnimeList;
import com.kttdevelopment.mal4j.TestProvider;
import com.kttdevelopment.mal4j.forum.*;
import org.junit.jupiter.api.*;

public class TestForumCategories {

    @SuppressWarnings("FieldCanBeLocal")
    private static MyAnimeList mal;
    private static ForumCategory category;

    @SuppressWarnings("ConstantConditions")
    @BeforeAll
    public static void beforeAll(){
        mal = TestProvider.getMyAnimeList();
        category = mal.getForumBoards().get(0);
    }

    @Test
    public void testCategory(){
        Assertions.assertNotNull(category.getTitle());
        Assertions.assertNotEquals("{}", category.toString());
    }

    @Test
    public void testBoard(){
        final ForumBoard board = category.getForumBoards()[2];
        Assertions.assertNotNull(board.getID());
        Assertions.assertNotNull(board.getTitle());
        Assertions.assertNotNull(board.getDescription());

        Assertions.assertNotEquals("{}", board.toString());

        Assertions.assertEquals(category, board.getCategory());
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test // not all forums have subboards
    public void testSubBoard(){
        final ForumSubBoard subBoard = category.getForumBoards()[2].getSubBoards()[0];
        Assertions.assertNotNull(subBoard.getID());
        Assertions.assertNotNull(subBoard.getTitle());

        Assertions.assertNotEquals("{}", subBoard.toString());
    }

}
