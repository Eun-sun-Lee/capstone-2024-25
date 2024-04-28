package capstone.allbom.medicine.service.dto;

import capstone.allbom.common.exception.BadRequestException;
import capstone.allbom.common.exception.DefaultErrorCode;
import capstone.allbom.common.util.Validator;
import capstone.allbom.medicine.domain.Medicine;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

import java.util.List;
import java.util.function.Predicate;

//@Builder
public record MedicineRequest(
        @NotNull(message = "약 이름은 필수입니다.")
        @Range(min = 1, max = 20, message = "약 이름의 길이를 확인해주세요.")
        String medicineName,
        @NotNull
        List<String> medicineTime
) {

    public MedicineRequest {
        Validator.notNull(medicineName, "medicineName");
        Validator.notNull(medicineTime, "medicineTime");
        Validator.minLength(medicineName, 1, "medicineName");
        Validator.maxLength(medicineName, 20, "medicineName");
        validateMedicineTime(medicineTime);
    }

    private void validateMedicineTime(List<String> medicineTime) {
        Predicate<String> isValidTime = time -> time.equals("아침") || time.equals("점심") || time.equals("저녁");

        if (medicineTime.stream().anyMatch(isValidTime.negate())) {
            throw new BadRequestException(DefaultErrorCode.INVALID_MEDICINE_TIME);
        }
    }
    public Medicine toDomain() {
        return new Medicine(
                null,
                null,
                medicineName,
                medicineTime
        );
    }
}
