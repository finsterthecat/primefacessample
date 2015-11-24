package com.brouwer.primefacessample.model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test DiscountCode JPA entity.
 *
 * @author tonybrouwer
 */
@RunWith(Arquillian.class)
public class DiscountCodePersistenceTest {

  @Deployment
  public static Archive<?> createDeployment() {
    return ShrinkWrap.create(WebArchive.class, "test.war")
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
  public void shouldFindAllDiscountCodesUsingJpqlQuery() throws Exception {
    // given
    String fetchingAllDiscountCodesInJpql = "select d from DiscountCode d"
            + " where d.discountCode in :codes "
            + " order by d.discountCode";

    // when
    System.out.println("Selecting (using JPQL)...");
    TypedQuery<DiscountCode> q = em.createQuery(fetchingAllDiscountCodesInJpql, DiscountCode.class);
    q.setParameter("codes", java.util.Arrays.asList(DISCOUNT_CODES));
    List<DiscountCode> discountCodes = q.getResultList();

    // then
    System.out.println("Found " + discountCodes.size() + " discount codes (using JPQL):");
    for (DiscountCode dc : discountCodes) {
      System.out.println(" - " + dc.getDiscountCode());
    }
    Assert.assertEquals(3, discountCodes.size());
  }
}
