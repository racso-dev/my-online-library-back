package com.marketpay;

import com.marketpay.persistence.entity.*;
import com.marketpay.persistence.repository.*;
import org.junit.Before;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

/**
 * Created by tchekroun on 10/07/2017.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MarketPayUnitTests {

    @Mock
    @Autowired
    private BusinessUnitRepository businessUnitRepository = Mockito.mock(BusinessUnitRepository.class);

    @Mock
    @Autowired
    private JobHistoryRepository jobHistoryRepository = Mockito.mock(JobHistoryRepository.class);

    @Mock
    @Autowired
    private OperationRepository operationRepository = Mockito.mock(OperationRepository.class);

    @Mock
    @Autowired
    private ShopRepository shopRepository = Mockito.mock(ShopRepository.class);

    @Mock
    @Autowired
    private BlockRepository blockRepository = Mockito.mock(BlockRepository.class);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        createBlockHistoryReturnMock();
        createBusinessReturnMock();
        createJobHistoryReturnMock();
        createOperationReturnMock();
        createShopRepositoryReturnMock();
    }

    public void createBusinessReturnMock() {
        BusinessUnit businessUnit = new BusinessUnit();
        businessUnit.setId(12l);
        Mockito.doReturn(Optional.of(businessUnit)).when(businessUnitRepository).findFirstByClientId(Matchers.anyString());
    }

    public void createOperationReturnMock() {
        Mockito.doReturn(new Operation()).when(operationRepository).save((Operation) Matchers.anyObject());
    }

    public void createShopRepositoryReturnMock() {
        Shop shop = new Shop();
        shop.setName("toot");
        Mockito.doReturn(Optional.of(shop)).when(shopRepository).findFirstByContractNumber(Matchers.anyString());
    }

    public void createJobHistoryReturnMock() {
        Mockito.doReturn(new JobHistory()).when(jobHistoryRepository).save((JobHistory) Matchers.anyObject());
    }

    public void createBlockHistoryReturnMock() {
        Mockito.doReturn(new Block()).when(blockRepository).save((Block) Matchers.anyObject());
    }
}
