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
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class FacilityService {

    private final FacilityRepository facilityRepository;

    @Qualifier("redisObjectTemplate")
    private final RedisTemplate<String, String> redisStringTemplate; // Object -> String
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

    public void saveFacilityToRedis(Facility facility) {
        GeoOperations<String, String> geoOperations = redisStringTemplate.opsForGeo();
        Point point = new Point(facility.getLongitude(), facility.getLatitude());
        String geoKey = facility.getType().toString();
        geoOperations.add(geoKey, point, facility.getId().toString());
    }

    public List<RedisGeoCommands.GeoLocation<String>> findNearestFacilities(double latitude, double longitude, int maxResults, String geoKey) {
        double searchRadiusInKilometers = 50.0;

        // 검색 옵션 정의: 좌표와 거리 포함, 오름차순 정렬, 최대 결과 개수 제한
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                .includeCoordinates()  // 좌표 포함
                .includeDistance()     // 거리 포함
                .sortAscending()       // 오름차순 정렬 (가까운 거리부터)
                .limit(maxResults);    // 최대 결과 수 제한

        // 중심 좌표와 반경 정의 (Point는 (longitude, latitude) 순서로 입력됨)
        Circle searchArea = new Circle(new Point(longitude, latitude), new Distance(searchRadiusInKilometers, RedisGeoCommands.DistanceUnit.KILOMETERS));

        // Redis에서 가장 가까운 시설 검색
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = redisStringTemplate.opsForGeo()
                .radius(geoKey, searchArea, args);

        // GeoResult에서 GeoLocation 객체만 추출하여 리스트로 반환
        return geoResults.getContent().stream()
                .map(GeoResult::getContent)  // GeoResult에서 GeoLocation 추출
                .collect(Collectors.toList());
    }
}
