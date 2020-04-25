package com.kakaopay.preexam.service.coupon;

import com.kakaopay.preexam.dto.account.AccountDto;
import com.kakaopay.preexam.dto.coupon.CouponDto;
import com.kakaopay.preexam.exception.AccountException;
import com.kakaopay.preexam.exception.BaseException;
import com.kakaopay.preexam.exception.CouponException;
import com.kakaopay.preexam.model.account.Account;
import com.kakaopay.preexam.model.coupon.Coupon;
import com.kakaopay.preexam.model.coupon.CouponInventory;
import com.kakaopay.preexam.model.coupon.CouponInventoryResult;
import com.kakaopay.preexam.model.coupon.CouponParams;
import com.kakaopay.preexam.model.response.RESPONSE_STATUS;
import com.kakaopay.preexam.repository.account.AccountRepository;
import com.kakaopay.preexam.repository.coupon.CouponInventoryRepository;
import com.kakaopay.preexam.repository.coupon.CouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;

@DisplayName("쿠폰 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {
    @InjectMocks
    CouponService couponService;

    @Mock
    CouponRepository couponRepository;

    @Mock
    CouponInventoryRepository couponInventoryRepository;

    @Mock
    AccountRepository accountRepository;

    @Mock
    BaseException baseException;

    @Mock
    AccountException accountException;

    @Mock
    CouponException couponException;

    private static final LocalDateTime nowDateTime = LocalDate.now().atTime(LocalTime.now().plusHours(1).getHour(), (LocalDateTime.now().getMinute() / 10) * 10);
    private static final LocalDateTime expireDateTime = LocalDateTime.now().with(LocalTime.MAX).plusYears(1);
    private static final LocalDateTime alreadyExpireDateTime = LocalDateTime.now().minusDays(1);

    // 로그인 mockup 데이터 생성
    private static final AccountDto accountDto = AccountDto.builder()
            .name("test")
            .password("dkahffkd!!")
            .build();

    // 쿠폰 mockup 데이터 생성
    private static final CouponDto couponDto = CouponDto.builder()
            .couponCode("A1B2-TT20-C3D4-F5G6")
            .isvalid(0)
            .type("CREATE")
            .createTime(nowDateTime)
            .expireTime(expireDateTime)
            .build();

    // 쿠폰 보관함 mockup 데이터 생성
    private static final CouponInventory couponInventory = CouponInventory.builder()
            .id(1L)
            .status("NOT_USED")
            .useTime(null)
            .createTime(nowDateTime)
            .expireTime(expireDateTime)
            .build();

    // 개인 쿠폰 보관함 mockup 데이터 생성
    private static final CouponInventoryResult couponInventoryResult = CouponInventoryResult.builder()
            .couponCode("A1B2-TT20-C3D4-F5G6")
            .status("NOT_USED")
            .expireTime(expireDateTime)
            .build();

    @Test
    @DisplayName("쿠폰 생성 테스트")
    public void testMakeCoupon() {
        CouponParams couponParams = new CouponParams();

        // 쿠폰이 정상 생성되었을 경우 에러 없음 확인
        couponParams.setCount(1000);
        assertDoesNotThrow(() ->
                couponService.makeCoupon(couponParams));

        verify(couponRepository, atMostOnce()).save(any());
    }

    @Test
    @DisplayName("쿠폰 생성시 요청 갯수가 잘못 입력되었을 때 에러 테스트")
    public void testMakeCouponCountWrongParameterException() {
        CouponParams couponParams = new CouponParams();
        couponParams.setCount(0);

        // 생성될 쿠폰 갯수를 1개 이하로 요청했을 경우 반환 예상되는 에러를 검증
        baseException = assertThrows(BaseException.class, () ->
                couponService.makeCoupon(couponParams));
        assertEquals(RESPONSE_STATUS.BAD_REQUEST.getMessage(), baseException.getMessage());
        assertEquals(RESPONSE_STATUS.BAD_REQUEST.getCode(), baseException.getErrorCode());
    }

    @Test
    @DisplayName("쿠폰 생성시 요청 갯수가 제한된 갯수를 초과하였을 경우 에러 테스트")
    public void testMakeCouponCountOverException() {
        CouponParams couponParams = new CouponParams();
        couponParams.setCount(1000001);

        // 생성될 쿠폰 갯수를 100만개 이상으로 요청했을 경우 반환 예상되는 에러를 검증
        couponException = assertThrows(CouponException.class, () ->
                couponService.makeCoupon(couponParams));
        assertEquals(RESPONSE_STATUS.COUPON_MAKE_COUNT_OVER.getMessage(), couponException.getMessage());
        assertEquals(RESPONSE_STATUS.COUPON_MAKE_COUNT_OVER.getCode(), couponException.getErrorCode());
    }

    @Test
    @DisplayName("쿠폰 지급 테스트")
    public void testCouponGive() throws Exception {
        // 사용자 검증 대비
        setAccountData();

        // 사용 가능 쿠폰 검증 대비
        when(couponRepository.findTop1ByIsvalidEqualsAndExpireTimeGreaterThanEqualOrderByCreateTimeAsc(0, nowDateTime)).thenReturn(
                Optional.of(Coupon.builder()
                        .id(1L)
                        .couponCode(couponDto.getCouponCode())
                        .isvalid(couponDto.getIsvalid())
                        .type(couponDto.getType())
                        .createTime(couponDto.getCreateTime())
                        .expireTime(couponDto.getExpireTime())
                        .build()));

        // 쿠폰 지급
        CouponParams couponParams = new CouponParams();
        couponParams.setAccountId(1L);

        // 쿠폰 지급 결과 에러 없음 확인
        assertDoesNotThrow(() ->
                couponService.couponGive(couponParams));

        verify(couponInventoryRepository, atLeastOnce()).save(any());
    }

    @Test
    @DisplayName("존재하지 않는 사용자 정보로 쿠폰 지급 시도시 에러 테스트")
    public void testCouponGiveAccountNotExistException() {
        CouponParams couponParams = new CouponParams();
        couponParams.setAccountId(1L);

        // 반환 예상되는 에러를 검증
        accountException = assertThrows(AccountException.class, () ->
                couponService.couponGive(couponParams));
        assertEquals(RESPONSE_STATUS.ACCOUNT_NOT_EXIST.getMessage(), accountException.getMessage());
        assertEquals(RESPONSE_STATUS.ACCOUNT_NOT_EXIST.getCode(), accountException.getErrorCode());
    }

    @Test
    @DisplayName("지급 가능한 쿠폰이 없을 때 쿠폰 지급 시도시 에러 테스트")
    public void testCouponGiveCouponNotEnoughException() {
        // 사용자 검증 대비
        setAccountData();

        CouponParams couponParams = new CouponParams();
        couponParams.setAccountId(1L);

        // 반환 예상되는 에러를 검증
        couponException = assertThrows(CouponException.class, () ->
                couponService.couponGive(couponParams));
        assertEquals(RESPONSE_STATUS.COUPON_NOT_ENOUGH.getMessage(), couponException.getMessage());
        assertEquals(RESPONSE_STATUS.COUPON_NOT_ENOUGH.getCode(), couponException.getErrorCode());
    }

    @Test
    @DisplayName("사용자 지급 쿠폰 조회 테스트")
    public void testGetGiveCouponList() throws Exception {
        // 사용자 검증 대비
        setAccountData();

        // 테스트 쿠폰 보관함 데이터 등록
        List<CouponInventoryResult> couponInventoryResultList = new ArrayList<>();
        couponInventoryResultList.add(couponInventoryResult);

        when(couponInventoryRepository.findUserCouponList(1L)).thenReturn(
                couponInventoryResultList);

        CouponParams couponParams = new CouponParams();
        couponParams.setAccountId(1L);

        // 검색된 결과와 데이터가 일치 하는지 검증
        assertArrayEquals(couponInventoryResultList.toArray(), couponService.getGiveCouponList(couponParams).toArray());

        // 쿠폰 지급 조회 결과 에러 없음 확인
        assertDoesNotThrow(() ->
                couponService.getGiveCouponList(couponParams));
    }

    @Test
    @DisplayName("존재하지 않는 사용자 정보로 지급된 쿠폰 조회 에러 테스트")
    public void testGetGiveCouponListAccountNotExistException() {
        CouponParams couponParams = new CouponParams();
        couponParams.setAccountId(1L);

        // 반환 예상되는 에러를 검증
        accountException = assertThrows(AccountException.class, () ->
                couponService.getGiveCouponList(couponParams));
        assertEquals(RESPONSE_STATUS.ACCOUNT_NOT_EXIST.getMessage(), accountException.getMessage());
        assertEquals(RESPONSE_STATUS.ACCOUNT_NOT_EXIST.getCode(), accountException.getErrorCode());
    }

    @Test
    @DisplayName("지급된 쿠폰 사용 테스트")
    public void testCouponRedeem() throws Exception {
        // 사용자 검증 대비
        setAccountData();

        // 메칭되는 쿠폰 보관함의 쿠폰정보 등록
        when(couponInventoryRepository.findMatchedCouponInventoryDetail(1L, couponInventoryResult.getCouponCode())).thenReturn(
                Optional.of(couponInventory));

        CouponParams couponParams = new CouponParams();
        couponParams.setAccountId(1L);
        couponParams.setCouponCode(couponInventoryResult.getCouponCode());

        // 지급된 쿠폰 사용 결과 에러 없음 확인
        assertDoesNotThrow(() ->
                couponService.couponRedeem(couponParams));
    }

    @Test
    @DisplayName("존재하지 않는 사용자 정보로 지급된 쿠폰 사용시 에러 테스트")
    public void testCouponRedeemAccountNotExistException() {
        CouponParams couponParams = new CouponParams();
        couponParams.setAccountId(1L);
        couponParams.setCouponCode(couponInventoryResult.getCouponCode());

        // 반환 예상되는 에러를 검증
        accountException = assertThrows(AccountException.class, () ->
                couponService.couponRedeem(couponParams));
        assertEquals(RESPONSE_STATUS.ACCOUNT_NOT_EXIST.getMessage(), accountException.getMessage());
        assertEquals(RESPONSE_STATUS.ACCOUNT_NOT_EXIST.getCode(), accountException.getErrorCode());
    }

    @Test
    @DisplayName("지급된 쿠폰 사용시 해당 쿠폰이 존재하지 않을 경우 에러 반환 테스트")
    public void testCouponRedeemCouponNotExistException() {
        // 사용자 검증 대비
        setAccountData();

        CouponParams couponParams = new CouponParams();
        couponParams.setAccountId(1L);
        couponParams.setCouponCode("N1O2-T3E4-X5I6-S7T8");

        // 반환 예상되는 에러를 검증
        couponException = assertThrows(CouponException.class, () ->
                couponService.couponRedeem(couponParams));
        assertEquals(RESPONSE_STATUS.COUPON_NOT_EXIST.getMessage(), couponException.getMessage());
        assertEquals(RESPONSE_STATUS.COUPON_NOT_EXIST.getCode(), couponException.getErrorCode());
    }

    @Test
    @DisplayName("지급한 쿠폰 사용시 이미 사용된 쿠폰 에러 반환 테스트")
    public void testCouponRedeemCouponAlreadyUsedException() {
        // 사용자 검증 대비
        setAccountData();

        // 메칭되는 쿠폰 보관함의 쿠폰정보 등록 (이미 사용처리된 형태로)
        when(couponInventoryRepository.findMatchedCouponInventoryDetail(1L, couponInventoryResult.getCouponCode())).thenReturn(
                Optional.of(CouponInventory.builder()
                        .id(1L)
                        .status("USED")
                        .useTime(nowDateTime)
                        .createTime(nowDateTime)
                        .expireTime(expireDateTime)
                        .build()));

        CouponParams couponParams = new CouponParams();
        couponParams.setAccountId(1L);
        couponParams.setCouponCode(couponInventoryResult.getCouponCode());

        // 반환 예상되는 에러를 검증
        couponException = assertThrows(CouponException.class, () ->
                couponService.couponRedeem(couponParams));
        assertEquals(RESPONSE_STATUS.COUPON_ALREADY_USED.getMessage(), couponException.getMessage());
        assertEquals(RESPONSE_STATUS.COUPON_ALREADY_USED.getCode(), couponException.getErrorCode());
    }

    @Test
    @DisplayName("지급된 쿠폰 사용 취소 테스트")
    public void testCouponRedeemCancel() {
        // 사용자 검증 대비
        setAccountData();

        // 메칭되는 쿠폰 보관함의 쿠폰정보 등록 (이미 사용처리된 형태로)
        when(couponInventoryRepository.findMatchedCouponInventoryDetail(1L, couponInventoryResult.getCouponCode())).thenReturn(
                Optional.of(CouponInventory.builder()
                        .id(1L)
                        .status("USED")
                        .useTime(nowDateTime)
                        .createTime(nowDateTime)
                        .expireTime(expireDateTime)
                        .build()));

        CouponParams couponParams = new CouponParams();
        couponParams.setAccountId(1L);
        couponParams.setCouponCode(couponInventoryResult.getCouponCode());

        // 지급된 쿠폰 사용 취소 결과 에러 없음 확인
        assertDoesNotThrow(() ->
                couponService.couponRedeemCancel(couponParams));
    }

    @Test
    @DisplayName("존재하지 않는 사용자 정보로 지급된 쿠폰 사용 취소시 에러 테스트")
    public void testCouponRedeemCancelAccountNotExistException() {
        CouponParams couponParams = new CouponParams();
        couponParams.setAccountId(1L);
        couponParams.setCouponCode(couponInventoryResult.getCouponCode());

        // 반환 예상되는 에러를 검증
        accountException = assertThrows(AccountException.class, () ->
                couponService.couponRedeemCancel(couponParams));
        assertEquals(RESPONSE_STATUS.ACCOUNT_NOT_EXIST.getMessage(), accountException.getMessage());
        assertEquals(RESPONSE_STATUS.ACCOUNT_NOT_EXIST.getCode(), accountException.getErrorCode());
    }

    @Test
    @DisplayName("지급된 쿠폰 사용 취소시 해당 쿠폰이 존재하지 않을 경우 에러 반환 테스트")
    public void testCouponRedeemCancelCouponNotExistException() {
        // 사용자 검증 대비
        setAccountData();

        CouponParams couponParams = new CouponParams();
        couponParams.setAccountId(1L);
        couponParams.setCouponCode("N1O2-T3E4-X5I6-S7T8");

        // 반환 예상되는 에러를 검증
        couponException = assertThrows(CouponException.class, () ->
                couponService.couponRedeemCancel(couponParams));
        assertEquals(RESPONSE_STATUS.COUPON_NOT_EXIST.getMessage(), couponException.getMessage());
        assertEquals(RESPONSE_STATUS.COUPON_NOT_EXIST.getCode(), couponException.getErrorCode());
    }

    @Test
    @DisplayName("지급된 쿠폰 사용 취소시 쿠폰 기간이 만료된 경우 취소 에러 반환 테스트")
    public void testCouponRedeemCancelCouponExpiredException() {
        // 사용자 검증 대비
        setAccountData();

        // 메칭되는 쿠폰 보관함의 쿠폰정보 등록 (이미 사용처리된 형태로)
        when(couponInventoryRepository.findMatchedCouponInventoryDetail(1L, couponInventoryResult.getCouponCode())).thenReturn(
                Optional.of(CouponInventory.builder()
                        .id(1L)
                        .status("USED")
                        .useTime(nowDateTime)
                        .createTime(nowDateTime)
                        .expireTime(alreadyExpireDateTime)
                        .build()));

        CouponParams couponParams = new CouponParams();
        couponParams.setAccountId(1L);
        couponParams.setCouponCode(couponInventoryResult.getCouponCode());

        // 반환 예상되는 에러를 검증
        couponException = assertThrows(CouponException.class, () ->
                couponService.couponRedeemCancel(couponParams));
        assertEquals(RESPONSE_STATUS.COUPON_EXPIRED.getMessage(), couponException.getMessage());
        assertEquals(RESPONSE_STATUS.COUPON_EXPIRED.getCode(), couponException.getErrorCode());
    }

    @Test
    @DisplayName("발급된 쿠폰 중 당일 만료된 쿠폰 목록 조회")
    public void testGetExpiredCouponList() throws Exception {
        LocalDateTime starTime = nowDateTime.with(LocalTime.MIN);

        // 테스트 쿠폰 보관함 데이터 등록
        List<CouponInventoryResult> couponInventoryResultList = new ArrayList<>();
        couponInventoryResultList.add(couponInventoryResult);

        when(couponInventoryRepository.findExpiredCouponList(starTime, nowDateTime)).thenReturn(
                couponInventoryResultList);

        // 발급된 쿠폰 중 당일 만료된 쿠폰 목록 조회 결과 에러 없음 확인
        assertDoesNotThrow(() ->
                couponService.getExpiredCouponList());
    }

    // 로그인 mockup 데이터 생성
    private void setAccountData() {
        when(accountRepository.findById(1L)).thenReturn(
                Optional.of(Account.builder()
                        .id(1L)
                        .name(accountDto.getName())
                        .password(accountDto.getPassword())
                        .status(0)
                        .signupTime(nowDateTime)
                        .build()));
    }
}
