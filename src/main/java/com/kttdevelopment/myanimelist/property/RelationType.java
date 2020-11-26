package com.kttdevelopment.myanimelist.property;

import com.kttdevelopment.myanimelist.anime.property.AnimeType;

/**
 * Represents a related type.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Ktt Development
 */
public enum RelationType {

    Sequel              ("sequel"),
    Prequel             ("prequel"),
    AlternativeSetting  ("alternative_setting"),
    AlternativeVersion  ("alternative_version"),
    SideStory           ("side_story"),
    ParentStory         ("parent_story"),
    Summary             ("summary"),
    FullStory           ("full_story");

    private final String field;

    RelationType(String field) {
        this.field = field;
    }

        /**
     * Returns the field name.
     *
     * @return field name
     *
     * @since 1.0.0
     */
    public final String field(){
        return field;
    }

    /**
     * Returns the field as an enum.
     *
     * @param string string
     * @return enum
     *
     * @since 1.0.0
     */
    public static RelationType asEnum(final String string){
        for(final RelationType value : values())
            if(value.field.equalsIgnoreCase(string))
                return value;
        return null;
    }

    @Override
    public String toString() {
        return "RelationType{" +
                "field='" + field + '\'' +
                '}';
    }
}
