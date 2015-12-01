package com.brouwer.primefacessample;

import com.brouwer.primefacessample.DiscountCodeController;
import com.brouwer.primefacessample.DiscountCodeFacade;
import com.brouwer.primefacessample.model.DiscountCode;
import com.brouwer.primefacessample.model.util.JsfUtil;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import static org.hamcrest.core.Is.*;
import org.hamcrest.core.IsInstanceOf;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

/**
 * Test DiscountCode JPA entity.
 *
 * @author tonybrouwer
 */
@RunWith(Arquillian.class)
public class DiscountCodeFacadeTest {

  @EJB
  DiscountCodeFacade discountCodeFacade;
  
  @Deployment
  public static Archive<?> createDeployment() {
    return ShrinkWrap.create(WebArchive.class, "test.war")
            .addPackage(JsfUtil.class.getPackage())
            .addPackage(DiscountCodeController.class.getPackage())
            .addPackage(DiscountCode.class.getPackage())
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
  }

  private static final String[] DISCOUNT_CODES = {
    "X",
    "Y",
    "Z"
  };

  private static final BigDecimal[] RATES = {
    new BigDecimal("12.0"),
    new BigDecimal("10.0"),
    new BigDecimal("9.0")
  };

  @PersistenceContext
  EntityManager em;

  @Inject
  UserTransaction utx;

  @Before
  public void preparePersistenceTest() throws Exception {
    clearData();
    insertData();
    startTransaction();
  }

  @After
  public void teardownPersistenceTest() throws Exception {
    if (utx.getStatus() != Status.STATUS_MARKED_ROLLBACK) {
      commitTransaction();
    }
  }

  private void startTransaction() throws Exception {
    utx.begin();
    em.joinTransaction();
  }

  public void commitTransaction() throws Exception {
    utx.commit();
  }

  private void clearData() throws Exception {
    startTransaction();
    Query q = em.createQuery("delete from DiscountCode d where d.discountCode in :codes");
    q.setParameter("codes", Arrays.asList(DISCOUNT_CODES));
    q.executeUpdate();
    commitTransaction();
    em.clear();
  }

  private void insertData() throws Exception {
    startTransaction();
    for (int i = 0; i < 3; i++) {
      DiscountCode dc = new DiscountCode(DISCOUNT_CODES[i], RATES[i]);
      em.persist(dc);
    }
    commitTransaction();
  }

  @Test
  /**
   * Should find discount code if it exists.
   */
  public void shouldFindDiscountCodeThatExists() throws Exception {
    DiscountCode dc = discountCodeFacade.findByDiscountCode("X");
    assertEquals(new BigDecimal("12.0"), dc.getRate());
  }
  
  //Rules get ignored. Not sure why. Settle on relying on @Test annotation.
  //@Rule
  //public ExpectedException thrown = ExpectedException.none();
  
  /**
   * Should get error on query for a not found discount code: '?'.
   * Expecting EJBTransactionRolledBackException caused by NoResultException.
   * 
   * @throws Exception 
   */
  @Test(expected = EJBTransactionRolledbackException.class)
  public void shouldFailOnNotFoundDiscountCode() throws Exception {
    //thrown.expect(EJBTransactionRolledbackException.class);
    //thrown.expectCause(is(IsInstanceOf.<Throwable>instanceOf(NoResultException.class)));
    discountCodeFacade.findByDiscountCode("?");
  }
}
