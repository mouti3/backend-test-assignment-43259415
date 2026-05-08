package com.telepaxx.assignment.roster;

import com.telepaxx.assignment.model.PatientRecord;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomInputStream;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Loads patient-related data from a folder of DICOM files.
 * <p>
 * TODO: Implement this class.
 * <p>
 * Read *.dcm files from the configured roster folder.
 * For each relevant file, extract at minimum:
 * - PatientID  (DICOM tag: Tag.PatientID)
 * - PatientName (DICOM tag: Tag.PatientName) — stored as "FamilyName^GivenName^..."
 * <p>
 * Expose data needed by PatientSearchService.
 * <p>
 * Important implementation decisions should be documented in NOTES.md.
 * <p>
 * Hint: use DicomInputStream from the dcm4che3 library (already on the classpath).
 */
@ApplicationScoped
public class RosterLoader {

    private static final Logger LOG = Logger.getLogger(RosterLoader.class);

    @ConfigProperty(name = "assignment.roster.path")
    String rosterPath;

    private List<PatientRecord> patientData;

    void onStartRosterLoader(@Observes StartupEvent event) {
        Path path = Paths.get(rosterPath);
        LOG.infof("Loading DICOM files from path %s...", path.toAbsolutePath());
        if (!Files.isDirectory(path)) {
            LOG.errorf("Failed to load DICOM files, the path %s is not a directory", path.toAbsolutePath());
            this.patientData = List.of();
            return;
        }
        try (Stream<Path> stream = Files.list(path)) {
            this.patientData = stream.map(this::setPatientRecord).filter(Objects::nonNull).toList();
            LOG.infof("Loaded %d patient Records from %s", this.patientData.size(), path.toAbsolutePath());
        } catch (Exception e) {
            LOG.errorf(e, "Failed to load DICOM files %s", path.toAbsolutePath());
            this.patientData = List.of();
        }
    }

    public List<PatientRecord> getAllPatientRecords() {
        return patientData;
    }

    private PatientRecord setPatientRecord(Path file) {
        try {
            DicomInputStream dis = new DicomInputStream(file.toFile());
            Attributes metadata = dis.readDataset();
            String patientId = metadata.getString(Tag.PatientID);
            String[] name = metadata.getString(Tag.PatientName).split("\\^");
            String firstName = getValueFromName(name, 0);
            String lastName = getValueFromName(name, 1);
            dis.close();
            return new PatientRecord(patientId, lastName, firstName, file.toAbsolutePath().toString());
        } catch (Exception e) {
            LOG.errorf(e, "Failed to read from DICOM file %s", file.toAbsolutePath());
            return null;
        }
    }

    private String getValueFromName(String[] name, int index) {
        return name.length > index && !name[index].isEmpty() ? name[index] : null;
    }
}
