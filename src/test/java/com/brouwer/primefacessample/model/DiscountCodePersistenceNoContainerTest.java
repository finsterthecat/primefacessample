package com.brouwer.primefacessample.model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Test DiscountCode JPA entity.
 *
 * @author tonybrouwer
 */
public class DiscountCodePersistenceNoContainerTest {

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

  EntityManager em;
  EntityTransaction transaction;

  @Before
  public void preparePersistenceTest() throws Exception {
    em = Persistence.createEntityManagerFactory("integration").createEntityManager();
    this.transaction = em.getTransaction();
    clearData();
    insertData();
    startTransaction();
  }

  @After
  public void teardownPersistenceTest() throws Exception {
    if (transaction.getRollbackOnly()) {
      commitTransaction();
    }
  }

  private void startTransaction() throws Exception {
    transaction.begin();
  }

  public void commitTransaction() throws Exception {
    transaction.commit();
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
    assertEquals(3, discountCodes.size());
  }

}
