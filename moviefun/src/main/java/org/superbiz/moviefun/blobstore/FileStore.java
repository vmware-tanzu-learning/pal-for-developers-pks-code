package org.superbiz.moviefun.blobstore;

import org.apache.commons.lang.StringUtils;
import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;


public class FileStore implements BlobStore {

    private final Tika tika = new Tika();
    private String albumRootDirectory;

    public FileStore(String albumRootDirectory) {
        this.albumRootDirectory = albumRootDirectory;
        if (StringUtils.isNotEmpty(albumRootDirectory)) {
            new File(albumRootDirectory).mkdirs();
        }
    }

    @Override
    public void put(Blob blob) throws IOException {
        File targetFile = new File(albumRootDirectory, blob.getName());

        targetFile.delete();
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();

        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            IOUtils.copy(blob.getInputStream(), outputStream);
        }
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        File file = new File(albumRootDirectory, name);

        if (!file.exists()) {
            return Optional.empty();
        }

        return Optional.of(new Blob(
            name,
            new FileInputStream(file),
            tika.detect(file)
        ));
    }

    @Override
    public void deleteAll() {

    }
}
