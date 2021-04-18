/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: Person.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.db.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.hedwin.db.Results;
import fr.hedwin.db.model.NamedIdElement;
import fr.hedwin.db.utils.Future;

import java.util.function.Function;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Person extends NamedIdElement {

    public static class ResultsPerson extends Results<Person> {
        public ResultsPerson(Function<Integer, Future<ResultsPage<Person>>> pageFunction) throws Exception {
            super(pageFunction);
        }
    }

    @JsonProperty("birthday")
    private String birthday;

    @JsonProperty("gender")
    private int gender;

    @JsonProperty("biography")
    private String biography;

    @JsonProperty("profile_path")
    private String profilePath;

    @JsonProperty("adult")
    private boolean adult;

    @JsonProperty("homepage")
    private String homepage;

    @JsonProperty("imdb_id")
    private String imdbID;

    public String getBirthday() {
        return birthday;
    }

    public int getGender() {
        return gender;
    }

    public String getBiography() {
        return biography;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getImdbID() {
        return imdbID;
    }

    @Override
    public String toString() {
        return "Person{" +
                "birthday='" + birthday + '\'' +
                ", gender=" + gender +
                ", biography='" + biography + '\'' +
                ", profilePath='" + profilePath + '\'' +
                ", adult=" + adult +
                ", homepage='" + homepage + '\'' +
                ", imdbID='" + imdbID + '\'' +
                '}';
    }
}
