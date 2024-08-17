package capstone.allbom.facility.domain;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
    Facility save(Facility facility);

    Optional<Facility> findById(Long facilityId);

    boolean existsById(Long id);

    List<Facility> findByType(FacilityType type); // 해당 시설유형의 모든 시설 객체 반환

    List<Facility> findAll();

    @Query("SELECT f FROM Facility f WHERE f.latitude BETWEEN :southWestLatitude AND :northEastLatitude AND f.longitude BETWEEN :southWestLongitude AND :northEastLongitude")
    List<Facility> findFacilitiesInRectangle(@Param("southWestLatitude") Double southWestLatitude,
                                             @Param("southWestLongitude") Double southWestLongitude,
                                             @Param("northEastLatitude") Double northEastLatitude,
                                             @Param("northEastLongitude") Double northEastLongitude);

    @Query("SELECT f FROM Facility f WHERE f.latitude BETWEEN :southWestLatitude AND :northEastLatitude AND f.longitude BETWEEN :southWestLongitude AND :northEastLongitude AND f.type = :facilityType")
    List<Facility> findFacilitiesInRectangleAndType(@Param("southWestLatitude") Double southWestLatitude,
                                                    @Param("southWestLongitude") Double southWestLongitude,
                                                    @Param("northEastLatitude") Double northEastLatitude,
                                                    @Param("northEastLongitude") Double northEastLongitude,
                                                    @Param("facilityType") FacilityType facilityType);

    @Query(value = "SELECT f.* FROM Facility f " +
            "WHERE ST_DWithin( " +
            "  CAST(ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326) AS geography), " +
            "  CAST(ST_SetSRID(ST_MakePoint(f.longitude, f.latitude), 4326) AS geography), :radius * 1000) " +
            "AND f.type = :facilityType " +
            "ORDER BY ST_Distance( " +
            "  CAST(ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326) AS geography), " +
            "  CAST(ST_SetSRID(ST_MakePoint(f.longitude, f.latitude), 4326) AS geography)) " +
            "LIMIT :limit",
            nativeQuery = true)
    List<Facility> findFacilitiesByPostgis(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radius") Double radius,
            @Param("limit") int limit,
            @Param("facilityType") String facilityType);


    @Query(value = "SELECT f.* FROM Facility f " +
            "WHERE ST_DWithin( " +
            "  CAST(ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326) AS geography), " +
            "  CAST(ST_SetSRID(ST_MakePoint(f.longitude, f.latitude), 4326) AS geography), :radius * 1000) " +
            "ORDER BY ST_Distance( " +
            "  CAST(ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326) AS geography), " +
            "  CAST(ST_SetSRID(ST_MakePoint(f.longitude, f.latitude), 4326) AS geography)) " +
            "LIMIT :limit",
            nativeQuery = true)
    List<Facility> findAllFacilitiesByPostgis(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radius") Double radius,
            @Param("limit") int limit);
}
