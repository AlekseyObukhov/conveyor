package com.credit.conveyor.service.imp;

import com.credit.conveyor.dto.LoanApplicationRequestDTO;
import com.credit.conveyor.dto.CreditDTO;
import com.credit.conveyor.dto.ScoringDataDTO;
import com.credit.conveyor.dto.EmploymentDTO;
import com.credit.conveyor.dto.EmploymentStatus;
import com.credit.conveyor.dto.LoanOfferDTO;
import com.credit.conveyor.dto.PaymentScheduleElement;
import com.credit.conveyor.dto.Position;
import com.credit.conveyor.dto.Gender;
import com.credit.conveyor.dto.MaritalStatus;
import com.credit.conveyor.exception.ScoringException;

import com.credit.conveyor.service.ConveyorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@PropertySource("classpath:application.properties")
public class ConveyorServiceImpl implements ConveyorService {
    @Value("${base.rate}")
    private String baseRate;

    private static final String INSURANCE_MULTIPLIER = "0.03";
    private static final String INSURANCE_DISCOUNT = "2.00";
    private static final String SALARY_CLIENT_DISCOUNT = "1.00";
    private static final Integer NUMBER_OF_PERIODS_PER_YEAR = 12;

    @Override
    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO requestDTO) {
        log.info("Method getOffers starts with requestDTO = {}", requestDTO);
        List<LoanOfferDTO> loanOfferDTOList = Stream.of(generateLoanOffer(1L, false, false, requestDTO),
                        generateLoanOffer(2L, true, false, requestDTO),
                        generateLoanOffer(3L, false, true, requestDTO),
                        generateLoanOffer(4L, true, true, requestDTO))
                .sorted(Comparator.comparing(LoanOfferDTO::getRate).reversed())
                .toList();
        log.info("Method getOffers return loanOfferDTOList = {}", loanOfferDTOList);
        return loanOfferDTOList;
    }

    private LoanOfferDTO generateLoanOffer(Long applicationId,
                                  Boolean isInsuranseEnabled,
                                  Boolean isSalaryClient,
                                  LoanApplicationRequestDTO requestDTO) {
        log.info("Method generateLoanOffer starts with applicationId = {}, isInsuranseEnabled = {}, " +
                "isSalaryClient = {}, requestDTO = {}", applicationId, isInsuranseEnabled, isSalaryClient, requestDTO);
        BigDecimal rate = new BigDecimal(baseRate);

        BigDecimal totalAmount = calculateTotalAmount(requestDTO.getAmount(), isInsuranseEnabled);
        log.info("totalAmount = {}", totalAmount);

        BigDecimal finalRate = calculateRate(isInsuranseEnabled, isSalaryClient, rate);
        log.info("finalRate = {}", finalRate);

        LoanOfferDTO loanOfferDTO =  LoanOfferDTO.builder()
                .applicationId(applicationId)
                .requestedAmount(requestDTO.getAmount())
                .totalAmount(totalAmount)
                .term(requestDTO.getTerm())
                .rate(finalRate)
                .isInsuranceEnabled(isInsuranseEnabled)
                .isSalaryClient(isSalaryClient)
                .monthlyPayment(calculateMonthlyPayment(totalAmount, requestDTO.getTerm(), finalRate)).build();
        log.info("Method generateLoanOffer return loanOfferDTO = {}", loanOfferDTO);
        return loanOfferDTO;
    }

    private BigDecimal calculateTotalAmount(BigDecimal amount, Boolean isInsuranceEnabled) {
        if (isInsuranceEnabled) {
            return amount.add(amount.multiply(new BigDecimal(INSURANCE_MULTIPLIER)));
        }
        else return amount;
    }

    private BigDecimal calculateRate(Boolean isInsuranceEnabled, Boolean isSalaryClient, BigDecimal rate) {
        if (isInsuranceEnabled) {
            log.info("rate decrease by {} because isInsuranseEnabled = {}", INSURANCE_DISCOUNT, isInsuranceEnabled);
            rate = rate.subtract(new BigDecimal(INSURANCE_DISCOUNT));
        }
        if (isSalaryClient) {
            log.info("rate decrease by {} because isSalaryClient = {}", SALARY_CLIENT_DISCOUNT, isSalaryClient);
            rate = rate.subtract(new BigDecimal(SALARY_CLIENT_DISCOUNT));
        }
        return rate;
    }

    /**
     * Расчет аннуитетного платежа
     * ЕП = СК * КА, где
     * СК - сумма кредита
     * КА - коэффициент аннуитета
     * КА = (МП * (1 + МП)^КП)/((1 + МП)^КП - 1), где
     * МП - месячная процентная ставка
     * КП - количество платежей
     */

    private BigDecimal calculateMonthlyPayment(BigDecimal totalAmount, Integer term, BigDecimal rate) {
        log.info("Start calculateMonthlyPayment with totalAmount = {}, term = {}, rate = {}", totalAmount, term, rate);
        BigDecimal monthlyRatePercent = rate.divide(new BigDecimal(NUMBER_OF_PERIODS_PER_YEAR), 5, RoundingMode.HALF_EVEN);

        BigDecimal monthlyRate = monthlyRatePercent.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_EVEN);

        BigDecimal coefficient = (monthlyRate.add(BigDecimal.ONE)).pow(term).setScale(6, RoundingMode.HALF_EVEN);

        BigDecimal annuityCoefficient = monthlyRate.multiply(coefficient).divide(coefficient.subtract(BigDecimal.ONE), RoundingMode.HALF_EVEN);

        BigDecimal monthlyPayment = totalAmount.multiply(annuityCoefficient).setScale(2, RoundingMode.HALF_EVEN);
        log.info("monthlyPayment = {}", monthlyPayment);
        log.info("End of monthly payment calculation");
        return monthlyPayment;
    }

    @Override
    public CreditDTO calculateCredit(ScoringDataDTO scoringDataDTO) {
        log.info("Method calculateCredit starts with scoringDataDTO = {}", scoringDataDTO);
        BigDecimal rate = scoring(scoringDataDTO);

        BigDecimal totalAmount = calculateTotalAmount(scoringDataDTO.getAmount(),
                scoringDataDTO.getIsInsuranceEnabled());

        BigDecimal finalRate = calculateRate(scoringDataDTO.getIsInsuranceEnabled(),
                scoringDataDTO.getIsSalaryClient(), rate);
        log.info("finalRate = {}", finalRate);

        Integer term = scoringDataDTO.getTerm();

        BigDecimal monthlyPayment = calculateMonthlyPayment(totalAmount,
                term, finalRate);

        List<PaymentScheduleElement> paymentScheduleElementList = calculateScheduleElement(totalAmount,
                term, finalRate, monthlyPayment);

        BigDecimal psk = calculatePSK(paymentScheduleElementList, totalAmount, term);

        CreditDTO creditDTO = CreditDTO.builder()
                .amount(totalAmount)
                .monthlyPayment(monthlyPayment)
                .psk(psk)
                .paymentSchedule(paymentScheduleElementList)
                .term(term)
                .rate(finalRate).isInsuranceEnabled(scoringDataDTO.getIsInsuranceEnabled())
                .isSalaryClient(scoringDataDTO.getIsSalaryClient()).build();
        log.info("Method calculateCredit return creditDTO = {}", creditDTO);
        return creditDTO;
    }

    private List<PaymentScheduleElement> calculateScheduleElement(BigDecimal totalAmount, Integer term,
                                                                  BigDecimal rate, BigDecimal monthlyPayment) {
        log.info("Method calculateScheduleElement starts");
        List<PaymentScheduleElement> paymentScheduleElementList = new ArrayList<>();

        BigDecimal remainingDebt = totalAmount.setScale(2, RoundingMode.HALF_EVEN);

        for (int i = 1; i < term + 1; i++) {
            LocalDate paymentDate = LocalDate.now().plusMonths(i);
            BigDecimal interestPayment = calculateInterest(remainingDebt, rate).setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment);
            remainingDebt = remainingDebt.subtract(debtPayment);

            PaymentScheduleElement paymentScheduleElement = PaymentScheduleElement.builder()
                            .number(i)
                            .date(paymentDate)
                            .totalPayment(monthlyPayment)
                            .remainingDebt(remainingDebt)
                            .interestPayment(interestPayment)
                            .debtPayment(debtPayment).build();
            log.info("add to List paymentScheduleElement = {}", paymentScheduleElement);
            paymentScheduleElementList.add(paymentScheduleElement);

        }
        log.info("Method calculateScheduleElement return paymentScheduleElementList = {}", paymentScheduleElementList);
        return paymentScheduleElementList;
    }

    private BigDecimal calculateInterest(BigDecimal remainingDebt, BigDecimal rate) {
        BigDecimal monthlyRateDiv100 = rate.divide(BigDecimal.valueOf(100), 4,RoundingMode.HALF_EVEN);
        BigDecimal monthlyRate = monthlyRateDiv100.divide(new BigDecimal(NUMBER_OF_PERIODS_PER_YEAR), 6, RoundingMode.HALF_EVEN);
        return remainingDebt.multiply(monthlyRate);
    }

    /**
     * Формула расчета ПСК:
     * (СВ/СЗ - 1)/N * 100, где
     * СВ — сумма всех выплат;
     * СЗ — сумма займа;
     * N — срок кредитования в годах;
     */
    private BigDecimal calculatePSK(List<PaymentScheduleElement> paymentScheduleElementList,
                                    BigDecimal totalAmount, Integer term) {
        log.info("============== Calculating PSK ==============");
        BigDecimal paymentAmount = paymentScheduleElementList
                .stream()
                .map(PaymentScheduleElement::getTotalPayment)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        log.info("paymentAmount = {}", paymentAmount);


        BigDecimal termInYears = BigDecimal.valueOf(term)
                        .divide(BigDecimal.valueOf(NUMBER_OF_PERIODS_PER_YEAR), 3, RoundingMode.HALF_EVEN);
        log.info("termInYears = {}", termInYears);

        BigDecimal intermediateCoefficient = paymentAmount.divide(totalAmount, 3, RoundingMode.HALF_EVEN)
                        .subtract(BigDecimal.ONE);
        log.info("intermediateCoefficient = {}", intermediateCoefficient);

        BigDecimal psk = intermediateCoefficient.divide(termInYears, 3, RoundingMode.HALF_EVEN)
                .multiply(BigDecimal.valueOf(100));
        log.info("psk = {}", psk);
        log.info("========= End calculating PSK =========");
        return psk;
    }

    /**
     * Рабочий статус: Безработный → отказ; Самозанятый → ставка увеличивается на 1; Владелец бизнеса → ставка увеличивается на 3
     * Позиция на работе: Менеджер среднего звена → ставка уменьшается на 2; Топ-менеджер → ставка уменьшается на 4
     * Сумма займа больше, чем 20 зарплат → отказ
     * Семейное положение: Замужем/женат → ставка уменьшается на 3; Разведен → ставка увеличивается на 1
     * Количество иждивенцев больше 1 → ставка увеличивается на 1
     * Возраст менее 20 или более 60 лет → отказ
     * Пол: Женщина, возраст от 35 до 60 лет → ставка уменьшается на 3; Мужчина, возраст от 30 до 55 лет → ставка уменьшается на 3; Не бинарный → ставка увеличивается на 3
     * Стаж работы: Общий стаж менее 12 месяцев → отказ; Текущий стаж менее 3 месяцев → отказ
     */
    private BigDecimal scoring(ScoringDataDTO scoringDataDTO) {
        log.info("========== Start scoring ===========");
        EmploymentDTO employment = scoringDataDTO.getEmployment();

        BigDecimal rate = new BigDecimal(baseRate);
        List<String> exceptionInfo = new ArrayList<>();

        if (employment.getEmploymentStatus() == EmploymentStatus.UNEMPLOYED) {
            exceptionInfo.add("Client unemployed");
        } else if (employment.getEmploymentStatus() == EmploymentStatus.SELF_EMPLOYED) {
            log.info("Rate increase by 1 percent because employmentStatus = SELF_EMPLOYED");
            rate = rate.add(BigDecimal.ONE);
        } else if (employment.getEmploymentStatus() == EmploymentStatus.BUSINESS_OWNER) {
            log.info("Rate increase by 3 percent because employmentStatus = BUSINESS_OWNER");
            rate = rate.add(BigDecimal.valueOf(3));
        }

        if (employment.getPosition() == Position.MID_MANAGER) {
            log.info("Rate decrease by 3 percent because position = MID_MANAGER");
            rate = rate.subtract(BigDecimal.valueOf(2));
        } else if (employment.getPosition() == Position.TOP_MANAGER) {
            log.info("Rate decrease by 4 percent because position = TOP_MANAGER");
            rate = rate.subtract(BigDecimal.valueOf(4));
        }

        if (scoringDataDTO.getAmount().compareTo(employment.getSalary().multiply(BigDecimal.valueOf(20))) > 0) {
            exceptionInfo.add("The salary is too small");
        }

        if (scoringDataDTO.getMaritalStatus() == MaritalStatus.MARRIED) {
            log.info("Rate decrease by 3 percent because maritalStatus = MARRIED");
            rate = rate.subtract(BigDecimal.valueOf(3));
        } else if (scoringDataDTO.getMaritalStatus() == MaritalStatus.DIVORCED) {
            log.info("Rate increase by 1 percent because maritalStatus = DIVORCED");
            rate = rate.add(BigDecimal.ONE);
        }

        if (scoringDataDTO.getDependentAmount() > 1) {
            log.info("Rate increase by 1 percent because dependentAmount > 1");
            rate = rate.add(BigDecimal.ONE);
        }

        int age = Period.between(scoringDataDTO.getBirthdate(), LocalDate.now()).getYears();
        if (age < 20) {
            exceptionInfo.add("Client too young");
        } else if (age > 60) {
            exceptionInfo.add("Client too old");
        }

        if (scoringDataDTO.getGender() == Gender.FEMALE && (age >= 35 && age <= 60)) {
            log.info("Rate decrease by 3 percent because gender = FEMALE and age between 35 and 60");
            rate = rate.subtract(BigDecimal.valueOf(3));
        } else if (scoringDataDTO.getGender() == Gender.MALE && (age >= 30 && age <= 55)) {
            log.info("Rate decrease by 3 percent because gender = MALE and age between 30 and 55");
            rate = rate.subtract(BigDecimal.valueOf(3));
        } else if (scoringDataDTO.getGender() == Gender.NON_BINARY) {
            log.info("Rate increase by 3 percent because gender = NON_BINARY");
            rate = rate.add(BigDecimal.valueOf(3));
        }

        if (employment.getWorkExperienceTotal() < 12) {
            exceptionInfo.add("Total work experience to small");
        }
        if (employment.getWorkExperienceCurrent() < 3) {
            exceptionInfo.add("Not enough current work experience");
        }
        if (!exceptionInfo.isEmpty()) {
            throw new ScoringException(exceptionInfo.toString());
        }
        log.info("Scoring ends with rate = {}", rate);
        return rate;
    }

}
