package pl.futurecollars.invoicing.util

import pl.futurecollars.invoicing.db.file.Configuration
import pl.futurecollars.invoicing.utils.FilesService
import pl.futurecollars.invoicing.utils.IdService
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

class IdServiceTest extends Specification {

    private Path idPath = Path.of(Configuration.ID_FILE)

    def "current id if file was empty"() {
        given:
        IdService idService = new IdService(new FilesService())

        expect:
        1 == idService.getCurrentIdAndIncrement()

        and:
        2 == idService.getCurrentIdAndIncrement()

        and:
        3 == idService.getCurrentIdAndIncrement()
    }

    def "current id from last number if file was not empty"() {
        given:
        Files.writeString(idPath, "16")
        IdService idService = new IdService(new FilesService())

        expect:
        17 == idService.getCurrentIdAndIncrement()

        and:
        18 == idService.getCurrentIdAndIncrement()

        and:
        19 == idService.getCurrentIdAndIncrement()
    }

    def "current id if it isn't file"() {
        given:
        IdService idService = new IdService(new FilesService())

        expect:
        1 == idService.getCurrentIdAndIncrement()

        and:
        2 == idService.getCurrentIdAndIncrement()

        and:
        3 == idService.getCurrentIdAndIncrement()
    }

    def cleanup() {
        Files.deleteIfExists(idPath)
    }

}