package pl.futurecollars.invoice.db.file;

import java.io.IOException;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoice.db.Database;
import pl.futurecollars.invoice.model.Invoice;
import pl.futurecollars.invoice.utils.FilesService;
import pl.futurecollars.invoice.utils.IdService;
import pl.futurecollars.invoice.utils.JsonService;

@Slf4j
@Configuration
@ConditionalOnProperty(value = "database.type", havingValue = "file")
public class FileDatabaseConfiguration {

  @Bean
  public FilesService filesService() {
    return new FilesService();
  }

  @Bean
  public JsonService jsonService() {
    return new JsonService();
  }

  @Bean
  public IdService idService(
      FilesService filesService,
      @Value("${database.idPath}") String idPathString) throws IOException {

    Path idPath = filesService.createFile(idPathString);
    return new IdService(idPath, filesService);
  }

  @Bean
  public Database<Invoice> fileRepository(
      FilesService filesService,
      JsonService jsonService,
      IdService idService,
      @Value("${database.path}") String dbPath) throws IOException {

    log.info("Running on file database");
    log.debug(dbPath);

    Path databasePath = filesService.createFile(dbPath);
    return new FileDatabase<>(databasePath, filesService, jsonService, idService, Invoice.class);
  }

}
