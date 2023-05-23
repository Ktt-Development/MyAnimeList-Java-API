package dev.katsute.mal4j.MangaTests;

import dev.katsute.mal4j.Fields;
import dev.katsute.mal4j.MyAnimeList;
import dev.katsute.mal4j.TestProvider;
import dev.katsute.mal4j.manga.MangaRanking;
import dev.katsute.mal4j.manga.property.MangaRankingType;
import dev.katsute.mal4j.manga.property.MangaType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

final class TestMangaRank {

    private static MyAnimeList mal;

    @BeforeAll
    static void beforeAll(){
        mal = TestProvider.getMyAnimeList();
    }

    @Test
    final void testRanking(){
        final List<MangaRanking> ranking =
            mal.getMangaRanking(MangaRankingType.Manga)
                .withLimit(1)
                .withFields(Fields.Manga.rank, Fields.Manga.media_type)
                .search();
        final MangaRanking first = ranking.get(0);
        assertEquals(1, first.getRanking());
        assertEquals(MangaType.Manga, first.getManga().getType());
        // assertNotNull(first.getPreviousRank());
        assumeTrue(first.getPreviousRank() != null, "Failed to get previous rank for Manga (this is an external issue, ignore this)");
    }

    @SuppressWarnings("EmptyMethod")
    @Test @Disabled
    final void testNSFW(){
        // difficult to test since NSFW is unlikely to be in top ranking
    }

}