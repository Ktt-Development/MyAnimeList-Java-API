package com.kttdevelopment.myanimelist.property;

import com.kttdevelopment.myanimelist.anime.property.AlternativeTitles;
import com.kttdevelopment.myanimelist.anime.RelatedAnime;
import com.kttdevelopment.myanimelist.manga.RelatedManga;

public interface ExtendedPreview extends Preview {

    AlternativeTitles AlternativeTitles();

    long getStartDate();

    long getEndDate();

    String getSynopsis();

    double getMeanRating();

    int getRank();

    int getPopularity();

    int getUserListingCount();

    int getUserScoringCount();

    NSFW getNSFW();

    long getCreatedAt();

    long getUpdatedAt();

    Genre[] getGenres();

    Picture[] getPictures();

    String getBackground();

    RelatedAnime[] getRelatedAnime();

    RelatedManga[] getRelatedManga();

}
