package capstone.allbom.facility.service;

import capstone.allbom.common.exception.BadRequestException;
import capstone.allbom.common.exception.DefaultErrorCode;
import capstone.allbom.common.exception.NotFoundException;
import capstone.allbom.facility.domain.Facility;
import capstone.allbom.facility.domain.FacilityRepository;
import capstone.allbom.facility.domain.FacilityType;
import capstone.allbom.facility.dto.FacilityListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class FacilityService {

    private final FacilityRepository facilityRepository;

    @Qualifier("redisObjectTemplate")
    private final RedisTemplate<String, String> redisTemplate; // Object -> String
    private static final String GEO_KEY = "facilities";

    @Transactional(readOnly = true)
    public Facility findByTypeAndId(final Long facilityId, final String type) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new NotFoundException(DefaultErrorCode.NOT_FOUND_MAP_ID));

        if (!facility.getType().toString().equals(type.toUpperCase())) {
            throw new BadRequestException(DefaultErrorCode.INVALID_FACILITY_TYPE_ID);
        }

        return facility;
    }

    @Transactional
    public Facility saveFacility(final Facility facility) {
        Facility savedFacility = facilityRepository.save(facility);
        return savedFacility;
    }

    public List<FacilityListResponse> getFacilities(Double SWlatitude, Double SWlongitude, Double NElatitude, Double NElongitude) {
        List<Facility> facilities = facilityRepository
                .findFacilitiesInRectangle(SWlatitude, SWlongitude, NElatitude, NElongitude);

        return facilities.stream()
                .map(FacilityListResponse::from)
                .toList();
    }

    public List<FacilityListResponse> getFacilitiesByType(Double SWlatitude, Double SWlongitude, Double NElatitude, Double NElongitude, String type) {
        List<Facility> facilities = facilityRepository
                .findFacilitiesInRectangleAndType(SWlatitude, SWlongitude, NElatitude, NElongitude, FacilityType.valueOf(type.toUpperCase()));

        return facilities.stream()
                .map(FacilityListResponse::from)
                .toList();
    }


//    public void saveFacilityToRedis(Facility facility) {
//        GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
//        Point point = new Point(facility.getLongitude(), facility.getLatitude());
//        geoOperations.add(GEO_KEY, point, facility.getType().toString());
//    }

    public void saveFacilityToRedis(Facility facility) {
        GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
        Point point = new Point(facility.getLongitude(), facility.getLatitude());
        String geoKey = facility.getType().toString();
        geoOperations.add(geoKey, point, facility.getId().toString());
    }
}
