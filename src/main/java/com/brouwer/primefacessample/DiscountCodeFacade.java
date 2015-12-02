/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.brouwer.primefacessample;

import com.brouwer.primefacessample.model.DiscountCode;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Discount Code Facade.
 * 
 * @author tonybrouwer
 */
@Stateless
public class DiscountCodeFacade extends AbstractFacade<DiscountCode> {

  @PersistenceContext(unitName = "com.brouwer_primefacessample_war_1.0-SNAPSHOTPU")
  EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public DiscountCodeFacade() {
    super(DiscountCode.class);
  }

  public DiscountCode findByDiscountCode(String code) {
    TypedQuery<DiscountCode> query  =
            this.getEntityManager().createNamedQuery(
                    "DiscountCode.findByDiscountCode", DiscountCode.class);
    query.setParameter("discountCode", code);
    return query.getSingleResult();
  }
}
