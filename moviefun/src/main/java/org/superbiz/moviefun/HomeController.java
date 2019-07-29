package org.superbiz.moviefun;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.superbiz.moviefun.albums.Album;
import org.superbiz.moviefun.albums.AlbumFixtures;
import org.superbiz.moviefun.albums.AlbumsBean;
import org.superbiz.moviefun.movies.Movie;
import org.superbiz.moviefun.movies.MovieFixtures;
import org.superbiz.moviefun.movies.MoviesBean;

import java.util.Map;

@Controller
public class HomeController {

    private final MoviesBean moviesBean;
    private final AlbumsBean albumsBean;
    private final MovieFixtures movieFixtures;
    private final AlbumFixtures albumFixtures;
    private String pageTitle;
    private String appVersion;

    public HomeController(MoviesBean moviesBean, AlbumsBean albumsBean,
                          MovieFixtures movieFixtures, AlbumFixtures albumFixtures,
                          @Value("${moviefun.title:Moviefun}") String pageTitle,
                          @Value("${moviefun.version:2.0}") String appVersion) {
        this.moviesBean = moviesBean;
        this.albumsBean = albumsBean;
        this.movieFixtures = movieFixtures;
        this.albumFixtures = albumFixtures;
        this.pageTitle = pageTitle;
        this.appVersion = appVersion;
    }

    @GetMapping("/")
    public String index(Map<String, Object> model) {
        model.put("PageTitle", pageTitle);
        model.put("AppVersion", appVersion);
        return "index";
    }

    @GetMapping("/setup")
    public String setup(Map<String, Object> model) {
        for (Movie movie : movieFixtures.load()) {
            moviesBean.addMovie(movie);
        }

        for (Album album : albumFixtures.load()) {
            albumsBean.addAlbum(album);
        }

        model.put("movies", moviesBean.getMovies());
        model.put("albums", albumsBean.getAlbums());
        model.put("PageTitle", pageTitle);
        model.put("AppVersion", appVersion);

        return "setup";
    }
}