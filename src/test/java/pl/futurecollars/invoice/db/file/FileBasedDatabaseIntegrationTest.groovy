package pl.futurecollars.invoice.db.file

import pl.futurecollars.invoice.configuration.FileDatabaseConfiguration
import pl.futurecollars.invoice.db.AbstractDatabaseTest
import pl.futurecollars.invoice.utils.FilesService
import pl.futurecollars.invoice.utils.IdService
import pl.futurecollars.invoice.utils.JsonService

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import static pl.futurecollars.invoice.TestHelpers.invoice

class FileBasedDatabaseIntegrationTest extends AbstractDatabaseTest {

    private final Path databasePath = Paths.get(FileDatabaseConfiguration.DATABASE_FILE)
    private final Path idPath = Paths.get(FileDatabaseConfiguration.ID_FILE)

    def setup() {
        database = new FileRepository(databasePath, new FilesService(), new JsonService(), new IdService(databasePath, idPath,new FilesService()))
    }

    def "should file based database writes invoices to correct file"() {
        when:
        database.save(invoice(4L))

        then:
        1 == Files.readAllLines(databasePath).size()

        when:
        database.save(invoice(5L))

        then:
        2 == Files.readAllLines(databasePath).size()
    }

    def cleanup() {
        Files.deleteIfExists(idPath)
        Files.deleteIfExists(databasePath)
    }

}