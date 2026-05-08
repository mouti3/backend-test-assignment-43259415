# Technical Decision Notes

## API Design

### Endpoint path: `GET /api/v1/patients/search`
The bootstrap used `/search`. It was changed to `/api/v1/patients/search` to follow REST conventions: namespace the API under a versioned prefix (`/api/v1`) so the contract can evolve without breaking existing clients, and group patient-related endpoints under `/patients`.

### `GET` with query parameters
Search criteria are passed as query parameters because this is a read operation. Query parameters keep searches bookmarkable, loggable, and cache-friendly, and they let the server be exercised easily from a browser or `curl`.

### `PatientSearchCriteria` as a `@BeanParam` record
Query parameters are bound into a `PatientSearchCriteria` record annotated with `@QueryParam`, and the resource receives it via `@BeanParam`. This keeps the resource method signature small and lets criteria evolve (new fields, validation) in one place.

### `PageResponse<T>` envelope
Responses are wrapped in a generic `PageResponse<T>(List<T> data, long total)` envelope so the API has a consistent shape and is forward-compatible with pagination (page/size/links) without changing the response root. The `total` field currently reports the size of the loaded roster — i.e. the dataset that was searched — rather than the matched-result count. This is intentional as a diagnostic signal; if true pagination is added later, `total` should be redefined as "total matches" to match conventional semantics.

---

## DICOM Parsing

### Eager loading at startup via `StartupEvent`
`RosterLoader` reads all DICOM files once at application startup by observing `StartupEvent`. The roster is treated as a static dataset, so loading it once and serving from memory keeps every search to an in-memory scan with no I/O.

### Files that fail to parse are skipped, not fatal
`setPatientRecord` returns `null` for any file that fails to read (corrupt, missing tags, wrong format) and the `null` is filtered out. The error is logged. A single bad file therefore cannot prevent the rest of the roster from loading.

### `try-with-resources` for `DicomInputStream`
The `DicomInputStream` is opened in a `try`-with-resources block so the underlying file handle is always released, even when `readDataset()` throws.

### `PatientName` is parsed defensively
DICOM stores `PatientName` (tag `00100010`) as `FamilyName^GivenName^MiddleName^Prefix^Suffix`. The implementation:
- treats a missing `PatientName` tag as an empty array (no NPE);
- splits on `^` and maps **index 0 → `lastName`**, **index 1 → `firstName`** (matching the DICOM spec);
- treats missing or empty components as `null`.
---

## Search Logic

### Case-insensitive equality
Comparisons use `String.equalsIgnoreCase`.
### Absent or blank criterion is treated as wildcard
### At least one criterion is required
If both criteria are blank/missing the service throws `MissingCriteriaException`.

---

## Error Handling

### Domain exceptions, mapped to HTTP responses
Validation failures inside the service are signalled with domain exceptions:
- `MissingCriteriaException` → HTTP 400, "Bad Request"
- `NoPatientsMatchException` → HTTP 404, "Not Found"

The service layer stays free of HTTP concerns; the mapping layer translates exceptions to responses.

### Dedicated `ExceptionMapper` per exception type
Each domain exception has its own `@Provider`-annotated JAX-RS `ExceptionMapper` that produces a structured `ErrorResponse` JSON body with `status`, `error`, `message`, and `path`.

A `GenericExceptionMapper<Throwable>` is registered as a catch-all fallback so unexpected errors still return a structured `500 Internal Server Error` body instead of leaking a stack trace.

---

## Response DTOs

### `PatientResponse(patientId, lastName, firstName, fileName)`

### `ErrorResponse` with `of(...)` factory
`ErrorResponse` is a record with a static `of(status, error, message, path)` factory.

---

## Testing

### Layered test strategy
- **Unit tests** (`PatientSearchServiceTest`) exercise `PatientSearchService` against a Mockito-mocked `RosterLoader`, isolating the search/filter logic from disk I/O.
- **Integration tests** (`PatientResourceTest`) use `@QuarkusTest` and REST Assured to verify the full HTTP stack: routing, query parameter binding, JSON serialisation, and exception mapping.

### Shared test fixtures
Constants and sample `PatientRecord` data live in `PatientsData` so unit and integration tests share a single source of truth for IDs, names, expected error messages, and the API path.
