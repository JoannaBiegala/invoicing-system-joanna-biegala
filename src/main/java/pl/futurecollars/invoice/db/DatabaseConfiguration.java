package pl.futurecollars.invoice.db;

import java.io.IOException;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoice.db.file.FileRepository;
import pl.futurecollars.invoice.db.memory.MemoryRepository;
import pl.futurecollars.invoice.service.InvoiceService;
import pl.futurecollars.invoice.utils.FilesService;
import pl.futurecollars.invoice.utils.IdService;
import pl.futurecollars.invoice.utils.JsonService;

@Configuration
public class DatabaseConfiguration {

  @Bean
  public FilesService filesService() {
    return new FilesService();
  }

  @Bean
  public JsonService jsonService() {
    return new JsonService();
  }

  @Bean
  @ConditionalOnProperty(value = "database.type", havingValue = "in-file")
  public IdService idService(
      FilesService filesService,
      @Value("${database.idpath}") String idPathString) throws IOException {

    Path idPath = filesService.createFile(idPathString);
    return new IdService(idPath, filesService);
  }

  @Bean
  @ConditionalOnProperty(value = "database.type", havingValue = "in-file")
  public Database fileRepository(
      FilesService filesService,
      JsonService jsonService,
      IdService idService,
      @Value("${database.path}") String dbPath) throws IOException {

    System.out.println("Running on fileRepository -> " + dbPath);

    Path databasePath = filesService.createFile(dbPath);
    return new FileRepository(databasePath, filesService, jsonService, idService);
  }

  @Bean
  @ConditionalOnProperty(value = "database.type", havingValue = "in-memory")
  public Database memoryRepository() {

    System.out.println("Running on memoryRepository");

    return new MemoryRepository();
  }

  @Bean
  public InvoiceService invoiceService(Database database) {
    return new InvoiceService(database);
  }

}
