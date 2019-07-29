package org.superbiz.moviefun.albums;

import org.apache.tika.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.superbiz.moviefun.blobstore.Blob;
import org.superbiz.moviefun.blobstore.BlobStore;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@Controller
@RequestMapping("/albums")
public class AlbumsController {
    private static final Logger logger = LoggerFactory.getLogger(AlbumsController.class);

    private final AlbumsBean albumsBean;
    private final BlobStore blobStore;
    private String pageTitle;
    private String appVersion;

    public AlbumsController(AlbumsBean albumsBean, BlobStore blobStore,
            @Value("${moviefun.title:Moviefun}") String pageTitle,
            @Value("${moviefun.version:2.0}") String appVersion) {
        this.albumsBean = albumsBean;
        this.blobStore = blobStore;
        this.pageTitle = pageTitle;
        this.appVersion = appVersion;
    }

    @GetMapping
    public String index(Map<String, Object> model) {
        logger.info("Get list of Albums");
        model.put("albums", albumsBean.getAlbums());
        model.put("PageTitle", pageTitle);
        model.put("AppVersion", appVersion);
        return "albums";
    }

    @GetMapping("/{albumId}")
    public String details(@PathVariable long albumId, Map<String, Object> model) {
        logger.info("Get Album details for {}", albumId);
        model.put("album", albumsBean.find(albumId));
        model.put("PageTitle", pageTitle);
        model.put("AppVersion", appVersion);
        return "albumDetails";
    }

    @PostMapping("/{albumId}/cover")
    public String uploadCover(@PathVariable long albumId, @RequestParam("file") MultipartFile uploadedFile) throws IOException {
        logger.info("Uploading Cover for Album {} of type {}, size {}", albumId,
                uploadedFile.getContentType(), uploadedFile.getSize());
        if (uploadedFile.getSize() > 0) {
            Blob coverBlob = new Blob(
                getCoverBlobName(albumId),
                uploadedFile.getInputStream(),
                uploadedFile.getContentType()
            );

            blobStore.put(coverBlob);
        }

        return format("redirect:/albums/%d", albumId);
    }

    @GetMapping("/{albumId}/cover")
    public HttpEntity<byte[]> getCover(@PathVariable long albumId) throws IOException, URISyntaxException {
        logger.info("Get Album Cover for {}", albumId);
        Optional<Blob> maybeCoverBlob = blobStore.get(getCoverBlobName(albumId));
        Blob coverBlob = maybeCoverBlob.orElseGet(() -> {
            logger.info("Using Default Album Cover for {}", albumId);
            return buildDefaultCoverBlob();
        });

        byte[] imageBytes = IOUtils.toByteArray(coverBlob.getInputStream());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(coverBlob.getContentType()));
        headers.setContentLength(imageBytes.length);

        return new HttpEntity<>(imageBytes, headers);
    }

    @DeleteMapping("/covers")
    public String deleteCovers() {
        logger.info("Delete all Album Covers");
        blobStore.deleteAll();
        return "redirect:/albums";
    }

    private Blob buildDefaultCoverBlob() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream input = classLoader.getResourceAsStream("default-cover.jpg");

        return new Blob("default-cover", input, IMAGE_JPEG_VALUE);
    }

    private String getCoverBlobName(@PathVariable long albumId) {
        return format("covers/%d", albumId);
    }
}
