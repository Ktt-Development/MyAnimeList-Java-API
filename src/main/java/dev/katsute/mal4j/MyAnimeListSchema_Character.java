/*
 * Copyright (C) 2023 Katsute <https://github.com/Katsute>
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
package dev.katsute.mal4j;

import dev.katsute.mal4j.Json.JsonObject;
import dev.katsute.mal4j.anime.Anime;
import dev.katsute.mal4j.character.Animeography;
import dev.katsute.mal4j.character.Character;
import dev.katsute.mal4j.property.Picture;

import java.util.Arrays;

@SuppressWarnings("unused")
abstract class MyAnimeListSchema_Character extends MyAnimeListSchema {

    static Character asCharacter(final MyAnimeList mal, final JsonObject schema){
        return schema == null ? null : new Character() {

            boolean draft = true; // if any field is null, try and fetch full values (only try once)

            private final Long id = schema.getLong("id");

            private String firstName;
            private String lastName;
            private String alternativeNames;
            private Picture mainPicture;
            private String biography;
            private Animeography[] animeography;

            {
                populate(schema);
            }

            @SuppressWarnings("DataFlowIssue")
            private void populate(){
                if(draft){
                    draft = false;
                    populate(((MyAnimeListImpl) mal).getCharacterSchema(id, ((String[]) null)));
                }
            }

            private void populate(final JsonObject schema){
                firstName        = schema.getString("first_name");
                lastName         = schema.getString("last_name");
                alternativeNames = schema.getString("alternative_name");
                mainPicture      = MyAnimeListSchema_Common.asPicture(mal, schema.getJsonObject("main_picture"));
                biography        = schema.getString("biography");

                animeography = adaptList(schema.getJsonArray("animeography"), s -> asAnimeography(mal, s), Animeography.class);
            }

            //

            @Override
            public final Long getID(){
                return id;
            }

            @Override
            public final String getFirstName(){
                if(firstName == null && draft)
                    populate();
                return firstName;
            }

            @Override
            public final String getLastName(){
                if(lastName == null && draft)
                    populate();
                return lastName;
            }

            @Override
            public final String[] getAlternativeNames(){
                if(alternativeNames == null && draft)
                    populate();
                return alternativeNames != null ? alternativeNames.split(", ") : null;
            }

            @Override
            public final Picture getMainPicture(){
                if(mainPicture == null && draft)
                    populate();
                return mainPicture;
            }

            @Override
            public final String getBiography(){
                if(biography == null && draft)
                    populate();
                return biography;
            }

            @Override
            public final Animeography[] getAnimeography(){
                if(animeography == null && draft)
                    populate();
                return animeography != null ? Arrays.copyOf(animeography, animeography.length) : null;
            }

            // additional methods

            @Override
            public final String toString(){
                return "Character{" +
                       "id=" + id +
                       ", firstName='" + firstName + '\'' +
                       ", lastName='" + lastName + '\'' +
                       ", alternativeNames='" + alternativeNames + '\'' +
                       ", mainPicture=" + mainPicture + '\'' +
                       ", biography='" + biography + '\'' +
                       ", animeography'" + Arrays.toString(animeography) + '\'' +
                       '}';
            }

        };
    }

    static Animeography asAnimeography(final MyAnimeList mal, final JsonObject schema){
        return schema == null ? null : new Animeography() {

            private final Anime anime = MyAnimeListSchema_Anime.asAnimePreview(mal, schema.getJsonObject("node"));
            private final String role = schema.getString("role");

            @Override
            public final Anime getAnime(){
                return anime;
            }

            @Override
            public final String getRole(){
                return role;
            }

            // additional methods

            @Override
            public final String toString(){
                return "Animeography{" +
                       "anime=" + anime +
                       ", role='" + role + '\'' +
                       '}';
            }

        };
    }

}