package pl.futurecollars.invoicing.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FilesService {

  public void createRepository(Path idPath, Path databasePath) throws IOException {
    createIdPathRepository(idPath);
    createDatabasePathRepository(databasePath);
  }

  private void createIdPathRepository(Path idPath) throws IOException {
    File file = new File(idPath.toString());
    file.getParentFile().mkdirs();
    file.createNewFile();
    writeToFile(idPath, String.valueOf(0L));
  }

  private void createDatabasePathRepository(Path databasePath) throws IOException {
    File databaseFile = new File(databasePath.toString());
    databaseFile.getParentFile().mkdirs();
    databaseFile.createNewFile();
  }

  public void appendLineToFile(Path path, String line) throws IOException {
    Files.write(path, (line + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
  }

  public void writeToFile(Path path, String line) throws IOException {
    Files.write(path, line.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
  }

  public void writeLinesToFile(Path path, List<String> lines) throws IOException {
    Files.write(path, lines, StandardOpenOption.TRUNCATE_EXISTING);
  }

  public List<String> readAllLines(Path path) throws IOException {
    return Files.readAllLines(path);
  }

  public String readLine(Path path) throws IOException {
    return Files.readString(path);
  }

}
