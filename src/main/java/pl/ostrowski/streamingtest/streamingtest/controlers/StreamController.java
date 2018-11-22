package pl.ostrowski.streamingtest.streamingtest.controlers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class StreamController {

    private final static String FILE_NAME = "python.exe";


    @GetMapping("/stream")
    public StreamingResponseBody getStream() throws IOException {

        Path path = Paths.get(FILE_NAME);

        StreamingResponseBody srb = (os) -> {

            int chunk_size = 1024;
            byte[] file = Files.readAllBytes(path);
            int chunksToLeft = file.length;
            int cursor = 0;

            while (chunksToLeft > 0) {

                if(cursor + chunk_size > file.length) {
                    chunk_size = file.length - cursor;
                }

                os.write(file, cursor, chunk_size);
                os.flush();
                cursor += chunk_size;
                chunksToLeft = chunksToLeft - chunk_size;
            }
        };

        return srb;
    }
}
