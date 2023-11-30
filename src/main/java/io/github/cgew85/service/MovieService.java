package io.github.cgew85.service;

import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import io.github.cgew85.domain.Movie;
import io.github.cgew85.domain.OmdbItem;
import io.github.cgew85.repository.MovieRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by cgew85 on 10.06.2018.
 */
@Service
@RequiredArgsConstructor
public class MovieService {

    private final OmdbService omdbService;
    private final MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie saveMovie(@NonNull final Movie movie) {
        return movieRepository.save(movie);
    }

    public void deleteMovie(@NonNull final Movie movie) {
        movieRepository.delete(movie);
    }

    public OmdbItem getOmdbItemByNameAndYear(final String name, final String year) {
        return prepareOmdbClient().get(omdbService.getOmdbApiKey(), name, year);
    }

    private OmdbClient prepareOmdbClient() {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Slf4jLogger(OmdbClient.class))
                .logLevel(Logger.Level.FULL)
                .target(OmdbClient.class, "http://www.omdbapi.com");
    }
}
