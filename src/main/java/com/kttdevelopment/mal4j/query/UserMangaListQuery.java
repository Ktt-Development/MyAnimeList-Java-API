/*
 * Copyright (C) 2021-2022 Katsute <https://github.com/Katsute>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.kttdevelopment.mal4j.query;

import com.kttdevelopment.mal4j.MyAnimeList;
import com.kttdevelopment.mal4j.anime.property.AnimeSort;
import com.kttdevelopment.mal4j.anime.property.AnimeStatus;
import com.kttdevelopment.mal4j.manga.MangaListStatus;
import com.kttdevelopment.mal4j.manga.property.MangaSort;
import com.kttdevelopment.mal4j.manga.property.MangaStatus;

/**
 * <b>Documentation:</b> <a href="https://myanimelist.net/apiconfig/references/api/v2#operation/users_user_id_mangalist_get">https://myanimelist.net/apiconfig/references/api/v2#operation/users_user_id_mangalist_get</a> <br>
 * Represents a user Manga list query.
 *
 * @see MyAnimeList#getUserMangaListing()
 * @see MyAnimeList#getUserMangaListing(String)
 * @see FieldQuery
 * @since 1.0.0
 * @version 2.9.0
 * @author Katsute
 */
public abstract class UserMangaListQuery extends FieldQuery<UserMangaListQuery,MangaListStatus> implements NSFW<UserMangaListQuery> {

    protected final String username;
    protected String sort;
    protected String status;
    protected Boolean nsfw;

    /**
     * Creates a user Manga search query. Applications do not use this constructor.
     *
     * @param username username
     *
     * @see MyAnimeList#getUserMangaListing()
     * @see MyAnimeList#getUserMangaListing(String)
     * @since 1.0.0
     */
    public UserMangaListQuery(final String username) {
        this.username = username;
    }

    /**
     * Sets the sorting option.
     *
     * @param sort sort
     * @return list query
     *
     * @see #sortBy(String)
     * @see MangaSort
     * @since 1.0.0
     */
    public final UserMangaListQuery sortBy(final MangaSort sort){
        return sortBy(sort.field());
    }

    /**
     * Sets the sorting option.
     *
     * @param sort sort
     * @return list query
     *
     * @see #sortBy(MangaSort)
     * @since 2.9.0
     */
    public final UserMangaListQuery sortBy(final String sort){
        this.sort = sort;
        return this;
    }

    /**
     * Sets the status filter.
     *
     * @param status status
     * @return list query
     *
     * @see #withStatus(String)
     * @see MangaStatus
     * @since 1.0.0
     */
    public final UserMangaListQuery withStatus(final MangaStatus status){
        return withStatus(status.field());
    }

    /**
     * Sets the status filter.
     *
     * @param status status
     * @return list query
     *
     * @see #withStatus(MangaStatus)
     * @since 2.9.0
     */
    public final UserMangaListQuery withStatus(final String status){
        this.status = status;
        return this;
    }

    @Override
    public final UserMangaListQuery includeNSFW(){
        return includeNSFW(true);
    }

    @Override
    public final UserMangaListQuery includeNSFW(final boolean nsfw){
        this.nsfw = nsfw;
        return this;
    }

}
