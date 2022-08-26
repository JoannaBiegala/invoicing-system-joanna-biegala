package pl.futurecollars.invoicing.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import pl.futurecollars.invoicing.db.file.Configuration;

public class IdService {

  private final FilesService filesService;
  private final Path idPath;
  private final Path databasePath;

  public IdService(FilesService filesService) {
    this.filesService = filesService;
    idPath = Path.of(Configuration.ID_FILE);
    databasePath = Path.of(Configuration.DATABASE_FILE);
  }

  public long getCurrentIdAndIncrement() {
    long currentId = 1L;
    try {
      if (Files.exists(idPath)) {
        currentId = Long.parseLong(filesService.readLine(idPath));
      } else {
        filesService.createRepository(idPath, databasePath);
      }
      filesService.writeToFile(idPath, String.valueOf(currentId + 1L));
    } catch (IOException e) {
      throw new RuntimeException("Unable to initialize repository", e);
    }
    return currentId + 1L;
  }

}
