package pl.futurecollars.invoicing.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import pl.futurecollars.invoicing.db.file.Configuration;

public class IdService {

  private final FilesService filesService;
  private final Path idPath;
  private final Path databasePath;

  private long currentId;

  public IdService(FilesService filesService) {
    this.filesService = filesService;
    this.idPath = Path.of(Configuration.ID_FILE);
    this.databasePath = Path.of(Configuration.DATABASE_FILE);
    this.currentId = 0L;
    try {
      if (Files.exists(idPath)) {
        currentId = Long.parseLong(filesService.readLine(idPath));
      } else {
        filesService.initDatabase(idPath, databasePath);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  public long getCurrentIdAndIncrement() {
    try {
      currentId++;
      filesService.writeToFile(idPath, String.valueOf(currentId));
    } catch (IOException e) {
      throw new RuntimeException("Unable to initialize repository", e);
    }
    return currentId;
  }

}
