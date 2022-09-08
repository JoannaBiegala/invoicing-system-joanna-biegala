package pl.futurecollars.invoice.configuration;

import java.io.IOException;
import java.nio.file.Path;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoice.db.Database;
import pl.futurecollars.invoice.db.file.FileRepository;
import pl.futurecollars.invoice.utils.FilesService;
import pl.futurecollars.invoice.utils.IdService;
import pl.futurecollars.invoice.utils.JsonService;

@Configuration
public class FileDatabaseConfiguration {

  public static final String ID_FILE = "test_db/nextId.txt";
  public static final String DATABASE_FILE = "test_db/invoices.json";

  @Bean
  public IdService idService(FilesService filesService) throws IOException {
    Path idPath = filesService.createFile(ID_FILE);
    return new IdService(idPath, filesService);
  }

  @Bean
  public Database fileRepository(FilesService filesService, JsonService jsonService, IdService idService) throws IOException {
    Path databasePath = filesService.createFile(DATABASE_FILE);
    return new FileRepository(databasePath, filesService, jsonService, idService);
  }

}
