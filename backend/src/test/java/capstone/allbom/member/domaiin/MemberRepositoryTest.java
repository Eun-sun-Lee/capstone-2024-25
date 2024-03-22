package capstone.allbom.member.domaiin;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 내장 in-memory 데이터베이스 사용 X
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 카카오로그인_아이디로_조회한다() {
        // given
        Member member = Member.builder()
                .name("userA")
//                .address("서울특별시 성북구 정릉동")
//                .detailAddress("77")
                .birthday(LocalDate.now())
                .latitude(37.6014512)
                .longitude(127.0166146)
                .kakaoEmail("eunsun2080@naver.com")
                .build();
        memberRepository.save(member);

        // then
        assertThat(member.getKakaoEmail()).isEqualTo(memberRepository.findBykakaoEmail("eunsun2080@naver.com").get().getKakaoEmail());
    }

    @Test
    void 일반로그인_아이디로_조회한다(){
        // given
        Member member = Member.builder()
                .name("userA")
                .loginId("eunsun")
                .build();
        memberRepository.save(member);

        // then
        assertThat(member.getLoginId()).isEqualTo(memberRepository.findByloginId("eunsun").get().getLoginId());
    }

}