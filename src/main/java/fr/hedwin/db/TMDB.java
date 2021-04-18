/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: TMDB.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.db;

import com.fasterxml.jackson.core.type.TypeReference;
import fr.hedwin.db.object.*;
import fr.hedwin.db.utils.CompletableFuture;
import fr.hedwin.db.utils.Future;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TMDB {

    /**
     * <pre>Français</pre>
     */
    private static final String FR = "fr-FR";

    /**
     * <pre>Anglais</pre>
     */
    private static final String US = "en-US";

    /**
     * <pre>Discover Movie</pre>
     * @see fr.hedwin.db.SortBy
     * @see Future
     * @see CompletableFuture
     * @see DbMovie.ResultsMovie
     * @param sortBy check SortBy class
     * @param with_genres list of genre id separate with comma
     * @param with_people list of person id separate with comma
     * @return future of ResultsMovie
     */
    public static Future<DbMovie.ResultsMovie> discoverMovie(SortBy sortBy, String with_genres, String with_people) {
        TmdbURL tmdbURL = new TmdbURL(TmdbURL.DISCORDER_MOVIE).addLanguage(FR);
        if (sortBy != null && !sortBy.equals(SortBy.nothing)) tmdbURL.addParams("sort_by", sortBy.getKey());
        if (with_genres != null) tmdbURL.addParams("with_genres", with_genres);
        if (with_people != null) tmdbURL.addParams("with_people", with_people);
        return CompletableFuture.async(() -> new DbMovie.ResultsMovie((page) -> ClientHttp.executeRequest(tmdbURL.editPage(page), new TypeReference<ResultsPage<DbMovie>>(){})));
    }
    public static Future<DbMovie.ResultsMovie> discoverMovie(SortBy sortBy, List<Genre> with_genres, String with_people) {
        List<String> strings = with_genres.stream().mapToInt(Genre::getId).mapToObj(String::valueOf).collect(Collectors.toList());
        return discoverMovie(sortBy, String.join(",", strings), with_people);
    }
    public static Future<DbMovie.ResultsMovie> discoverMovie(SortBy sortBy, List<String> with_genres, List<String> with_people) {
        return discoverMovie(sortBy, String.join(",", with_genres), String.join(",", with_people));
    }

    /**
     * <pre>Get Movie</pre>
     * @see Future
     * @see CompletableFuture
      @see DbMovie
     * @param id specify id movie
     * @return future of DbMovie
     */
    public static Future<DbMovie> getMovie(int id) {
        TmdbURL tmdbURL = new TmdbURL(TmdbURL.MOVIE+id).addLanguage(FR);
        return ClientHttp.executeRequest(tmdbURL, DbMovie.class);
    }

    /**
     * <pre>Get Movie Videos</pre>
     * @see Future
     * @see CompletableFuture
     * @see fr.hedwin.db.object.Videos
     * @param id specify id movie
     * @return future of Videos
     */
    public static Future<Videos> getMovieVideos(int id){
        TmdbURL tmdbURL = new TmdbURL(TmdbURL.MOVIE+id+"/videos").addLanguage(FR);
        return ClientHttp.executeRequest(tmdbURL, Videos.class);
    }

    /**
     * <pre>Get Movie Credits</pre>
     * @see Future
     * @see CompletableFuture
     * @see fr.hedwin.db.object.Credits
     * @param id specify id movie
     * @return future of Credits
     */
    public static Future<Credits> getMovieCredits(int id){
        TmdbURL tmdbURL = new TmdbURL(TmdbURL.MOVIE+id+"/credits").addLanguage(FR);
        return ClientHttp.executeRequest(tmdbURL, Credits.class);
    }


    /**
     * <pre>Get Tv Series</pre>
     * @see Future
     * @see CompletableFuture
     * @see DbSerie
     * @param id specify id series
     * @return future of DbSerie
     */
    public static Future<DbSerie> getTvSeries(int id) {
        TmdbURL tmdbURL = new TmdbURL(TmdbURL.TVSERIE+id).addLanguage(FR);
        return ClientHttp.executeRequest(tmdbURL, DbSerie.class);
    }

    /**
     * <pre>Get Tv Series Videos</pre>
     * @see Future
     * @see CompletableFuture
     * @see fr.hedwin.db.object.Videos
     * @param id specify id series
     * @return future of Videos
     */
    public static Future<Videos> getTvSeriesVideos(int id){
        TmdbURL tmdbURL = new TmdbURL(TmdbURL.TVSERIE+id+"/videos").addLanguage(FR);
        return ClientHttp.executeRequest(tmdbURL, Videos.class);
    }


    /**
     * <pre>Get Person</pre>
     * @see Future
     * @see CompletableFuture
     * @see fr.hedwin.db.object.Person
     * @param id specify id person
     * @return future of Person
     */
    public static Future<Person> getPerson(int id) {
        TmdbURL tmdbURL = new TmdbURL(TmdbURL.PERSON+id).addLanguage(FR);
        return ClientHttp.executeRequest(tmdbURL, Person.class);
    }


    /**
     * <pre>Search Movies - ResultsMovie</pre>
     * @see Future
     * @see CompletableFuture
     * @see DbMovie.ResultsMovie
     * @param request specify title movie name
     * @return future of ResultsMovie
     */
    public static Future<DbMovie.ResultsMovie> searchMovie(String request) {
        TmdbURL tmdbURL = new TmdbURL(TmdbURL.SEARCH_MOVIE).addLanguage(FR).addParams("query", request.replace(" ", "+"));
        return CompletableFuture.async(() -> new DbMovie.ResultsMovie((page) -> ClientHttp.executeRequest(tmdbURL.editPage(page), new TypeReference<ResultsPage<DbMovie>>(){})));
    }

    /**
     * <pre>Search TV Series - ResultsTVSerie</pre>
     * @see Future
     * @see CompletableFuture
     * @see DbSerie.ResultsTVSerie
     * @param request specify title series name
     * @return future of ResultsTVSerie
     */
    public static Future<DbSerie.ResultsTVSerie> searchTvSerie(String request) {
        TmdbURL tmdbURL = new TmdbURL(TmdbURL.SEARCH_TVSERIE).addLanguage(FR).addParams("query", request.replace(" ", "+"));
        return CompletableFuture.async(() -> new DbSerie.ResultsTVSerie((page) -> ClientHttp.executeRequest(tmdbURL.editPage(page), new TypeReference<ResultsPage<DbSerie>>(){})));
    }

    /**
     * <pre>Search Persons - ResultsPerson</pre>
     * @see Future
     * @see CompletableFuture
     * @see DbSerie.ResultsTVSerie
     * @param request specify person name
     * @return future of ResultsPerson
     */
    public static Future<Person.ResultsPerson> searchPerson(String request) {
        TmdbURL tmdbURL = new TmdbURL(TmdbURL.SEARCH_PERSON).addLanguage(FR).addParams("query", request.replace(" ", "+"));
        return CompletableFuture.async(() -> new Person.ResultsPerson((page) -> ClientHttp.executeRequest(tmdbURL.editPage(page), new TypeReference<ResultsPage<Person>>(){})));
    }

    /**
     * <pre>Get Image</pre>
     * @see Image
     * @param path poster_path or backdrop_path, example: /egzi56ef56456fz.png
     * @param size specify size or if null original size
     * @return Image
     * @throws Exception if it doesn't find image
     */
    public static Image getImage(String path, Integer size) throws Exception {
        if(path == null) throw new Exception("Chemin de l'image indéfinie !");
        return ClientHttp.loadImage("https://image.tmdb.org/t/p/"+(size != null ? "w"+size : "original")+path);
    }

    /**
     * <pre>Get Genre List</pre>
     * @see Genre.GenreList
     * @see Future
     * @see CompletableFuture
     * @return future of GenreList
     */
    public static Future<Genre.GenreList> getGenres(){
        TmdbURL tmdbURL = new TmdbURL(TmdbURL.GENRES_MOVIE).addLanguage(FR);
        return ClientHttp.executeRequest(tmdbURL, Genre.GenreList.class);
    }

    /**
     * <pre>Get Find ID</pre>
     * @see Find
     * @see Future
     * @see CompletableFuture
     * @return future of Find
     */
    public static Future<Find> find(int id){
        TmdbURL tmdbURL = new TmdbURL(TmdbURL.FIND+id).addLanguage(FR).addParams("external_source", "imdb_id");
        return ClientHttp.executeRequest(tmdbURL, Find.class);
    }

    public static List<Genre> getGenresSorted(){
        try {
            return getGenres().call().getGenres().stream()
                    .sorted((g1, g2) -> Comparator.comparing(Genre::getName).compare(g1, g2)).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
