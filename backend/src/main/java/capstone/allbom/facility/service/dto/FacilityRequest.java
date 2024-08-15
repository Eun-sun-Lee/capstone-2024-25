package capstone.allbom.facility.service.dto;

import capstone.allbom.facility.domain.Facility;
import capstone.allbom.facility.domain.FacilityType;

public record FacilityRequest(
        String name,
        String type,
        String address,
        String phoneNumber,
        Double latitude,
        Double longitude
) {
    public Facility toFacility() {
        return Facility.builder()
                .name(name)
                .type(FacilityType.valueOf(type.toUpperCase()))
                .address(address)
                .phoneNumber(phoneNumber)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

//    private static long idCounter = 0;
//
//    // 새로운 id를 생성하는 메서드
//    private static synchronized long generateId() {
//        return ++idCounter;
//    }

    public Facility toFacilityRedis() {
//        long facilityId = generateId();
        return Facility.builder()
                .type(FacilityType.valueOf(type.toUpperCase()))
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
